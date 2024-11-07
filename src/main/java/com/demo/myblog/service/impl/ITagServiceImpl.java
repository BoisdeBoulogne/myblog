package com.demo.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.myblog.entry.result.Result;
import com.demo.myblog.entry.table.Tag;
import com.demo.myblog.entry.table.Tag2Article;
import com.demo.myblog.mapper.Tag2ArticleMapper;
import com.demo.myblog.mapper.TagMapper;
import com.demo.myblog.service.ITagService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ITagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    @Resource
    private Tag2ArticleMapper tag2ArticleMapper;

    @Resource
    private IArticleServiceImpl articleServiceImpl;

    @Override
    public Result getTags() {
        return Result.ok(list());
    }

    @Override
    public Result getArticlesById(Integer id) {
        LambdaQueryWrapper<Tag2Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag2Article::getTagId, id);
        List<Integer> articleIds = tag2ArticleMapper.selectList(wrapper).stream().map(Tag2Article::getArticleId).toList();
        return Result.ok(articleServiceImpl.getArticlePerList(articleIds));
    }


}
