package com.demo.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.myblog.entry.dto.ArticleUploadDTO;
import com.demo.myblog.entry.result.Result;
import com.demo.myblog.entry.table.*;
import com.demo.myblog.entry.vo.ArticleDetailVo;
import com.demo.myblog.entry.vo.ArticlePerVo;
import com.demo.myblog.enums.AppEnum;
import com.demo.myblog.exception.SystemException;
import com.demo.myblog.mapper.*;
import com.demo.myblog.service.IArticleService;
import com.demo.myblog.utils.SecurityUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Resource
    private TagMapper tagMapper;
    @Resource
    private Tag2ArticleMapper tag2ArticleMapper;
    @Resource
    private HistoryMapper historyMapper;
    @Resource
    private CollectMapper collectMapper;

    @Override
    public Result upload( ArticleUploadDTO article) {
        Article articleEntity = new Article();
        BeanUtils.copyProperties(article, articleEntity);
        log.info(articleEntity.toString());
        articleEntity.setUserId(SecurityUtils.getUserId());
        this.save(articleEntity);
        return Result.ok();
    }

    @Override
    public Result getArticle(Integer id) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId, id);
        boolean collected = false;

        Article articleEntity = this.getOne(queryWrapper);

        if (articleEntity == null) {
            throw new SystemException(AppEnum.WRONG_QUERY);
        }

        try {
            Integer userId = SecurityUtils.getUserId();
            saveOrUpdateHistory(userId,id);
            LambdaQueryWrapper<Collect> collectQueryWrapper = new LambdaQueryWrapper<>();
            collectQueryWrapper.eq(Collect::getUserId, userId);
            collectQueryWrapper.eq(Collect::getArticleId, id);
            collected = collectMapper.selectCount(collectQueryWrapper) > 0;
        } catch (Exception e) {

            log.error("Failed to insert or update history for article: {}", id, e);
        }

        articleEntity.setViews(articleEntity.getViews() + 1);
        ArticleDetailVo detailVo = new ArticleDetailVo();
        BeanUtils.copyProperties(articleEntity, detailVo);
        update(articleEntity, queryWrapper);

        detailVo.setTags(getTagNames(id));
        detailVo.setCollected(collected);
        return Result.ok(detailVo);
    }

    @Override
    public Result deleteArticle(Integer id) {
        canChange(id);
        boolean deleted =  removeById(id);
        if (!deleted){
            throw new SystemException(AppEnum.OTHER_REASON);
        }
        LambdaQueryWrapper<Tag2Article> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(Tag2Article::getArticleId, id);
        tag2ArticleMapper.delete(queryWrapper2);

        LambdaQueryWrapper<History> queryWrapper3 = new LambdaQueryWrapper<>();
        queryWrapper3.eq(History::getArticleId, id);
        historyMapper.delete(queryWrapper3);
        return Result.ok();
    }

    @Override
    public Result updateArticle(Integer id, ArticleUploadDTO uploadDTO) {
        Article articleEntity = canChange(id);
        articleEntity.setTitle(uploadDTO.getTitle());
        articleEntity.setContent(uploadDTO.getContent());
        articleEntity.setUpdateTime(new Date());
        this.updateById(articleEntity);
        return Result.ok();
    }

    private Article canChange(Integer ArticleId) {
        Integer userId = SecurityUtils.getUserId();
        Article articleEntity = this.getById(ArticleId);
        if (articleEntity == null) {
            throw new SystemException(AppEnum.NO_SUCH_ENTRY);
        }
        Integer writerId = articleEntity.getUserId();
        if (!userId.equals(writerId)) {
            throw new SystemException(AppEnum.NON_RIGHT);
        }
        return articleEntity;
    }



    private void saveOrUpdateHistory(Integer userId,Integer articleId) {
        LambdaQueryWrapper<History> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(History::getUserId, userId);
        queryWrapper.eq(History::getArticleId, articleId);

        History history = historyMapper.selectOne(queryWrapper);
        if (history == null) {
            History insert = new History(new Date(), userId, articleId);
            historyMapper.insert(insert);
            return;
        }
        history.setDate(new Date());
        historyMapper.update(history,queryWrapper);
    }

    public List<ArticlePerVo> getArticlePerList(List<Integer> articleIds) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Article::getId, articleIds);

        List<Article> articles = this.list(queryWrapper);
        return articles.stream()
                .map(article -> {
                    LambdaQueryWrapper<Collect> collectQueryWrapper = new LambdaQueryWrapper<>();
                    collectQueryWrapper.eq(Collect::getArticleId, article.getId());
                    ArticlePerVo articlePerVo = new ArticlePerVo();
                    BeanUtils.copyProperties(article, articlePerVo);

                    Integer collectCount = collectMapper.selectCount(collectQueryWrapper).intValue();

                    articlePerVo.setContentPre(getContentPreview(article));
                    articlePerVo.setCollects(collectCount);
                    articlePerVo.setTags(getTagNames(article.getId()));
                    return articlePerVo;
                })
                .collect(Collectors.toList());
    }

    private String getContentPreview(Article article) {
        return article.getContent().length() > 50 ? article.getContent().substring(0, 50) : article.getContent();
    }

    private List<String> getTagNames(Integer articleId) {
        LambdaQueryWrapper<Tag2Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag2Article::getArticleId, articleId);
        return tag2ArticleMapper.selectList(queryWrapper).stream().map(
                tag2Article -> {
                    LambdaQueryWrapper<Tag> tagQueryWrapper = new LambdaQueryWrapper<>();
                    tagQueryWrapper.eq(Tag::getId, tag2Article.getTagId());
                    Tag tag = tagMapper.selectOne(tagQueryWrapper);
                    return tag.getName();
                }
        ).collect(Collectors.toList());
    }
}
