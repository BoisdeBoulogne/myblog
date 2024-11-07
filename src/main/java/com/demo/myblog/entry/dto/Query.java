package com.demo.myblog.entry.dto;

import lombok.Data;

import java.util.List;

@Data
public class Query {
    private String author;
    private String title;
    private String content;
}
