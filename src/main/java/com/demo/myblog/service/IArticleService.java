package com.demo.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.myblog.entry.dto.ArticleUploadDTO;
import com.demo.myblog.entry.result.Result;
import com.demo.myblog.entry.table.Article;
import jakarta.validation.Valid;

public interface IArticleService extends IService<Article> {
    Result upload(ArticleUploadDTO article);

    Result getArticle(Integer id);

    Result deleteArticle(Integer id);

    Result updateArticle(Integer id, ArticleUploadDTO uploadDTO);
}
