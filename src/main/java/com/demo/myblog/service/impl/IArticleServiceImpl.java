package com.demo.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.myblog.entry.dto.ArticleUploadDTO;
import com.demo.myblog.entry.result.Result;
import com.demo.myblog.entry.table.Article;
import com.demo.myblog.mapper.ArticleMapper;
import com.demo.myblog.service.IArticleService;
import com.demo.myblog.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Override
    public Result upload( ArticleUploadDTO article) {
        Article articleEntity = new Article();
        BeanUtils.copyProperties(article, articleEntity);
        log.info(articleEntity.toString());
        articleEntity.setUserId(SecurityUtils.getUserId());
        this.save(articleEntity);
        return Result.ok();
    }


}
