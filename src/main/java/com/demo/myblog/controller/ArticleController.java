package com.demo.myblog.controller;

import com.demo.myblog.entry.dto.ArticleUploadDTO;
import com.demo.myblog.entry.result.Result;
import com.demo.myblog.entry.table.Article;
import com.demo.myblog.service.IArticleService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Resource
    private IArticleService articleService;
    @PostMapping("/upload")
    public Result upload(@RequestBody @Valid ArticleUploadDTO article) {
        return articleService.upload(article);
    }



}
