package com.demo.myblog.controller;

import com.demo.myblog.entry.dto.ArticleUploadDTO;
import com.demo.myblog.entry.result.Result;
import com.demo.myblog.entry.table.Article;
import com.demo.myblog.service.IArticleService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Resource
    private IArticleService articleService;
    @PostMapping("/upload")
    public Result upload(@RequestBody @Valid ArticleUploadDTO article) {
        return articleService.upload(article);
    }

    @GetMapping("/get/{id}")
    public Result getArticle(@PathVariable Integer id) {
        return articleService.getArticle(id);
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteArticle(@PathVariable Integer id) {
        return articleService.deleteArticle(id);
    }

    @PutMapping("/update/{id}")
    public Result updateArticle(@PathVariable Integer id,@RequestBody ArticleUploadDTO uploadDTO){
        return articleService.updateArticle(id,uploadDTO);
    }

}
