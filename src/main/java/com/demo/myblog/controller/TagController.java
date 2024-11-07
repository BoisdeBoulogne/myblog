package com.demo.myblog.controller;

import com.demo.myblog.entry.result.Result;
import com.demo.myblog.service.ITagService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
public class TagController {
    @Resource
    ITagService tagService;
    @GetMapping()
    public Result getTags() {
        return tagService.getTags();
    }

    @GetMapping("/{id}")
    public Result getArticlesByTagId(@PathVariable Integer id) {
        return tagService.getArticlesById(id);
    }
}
