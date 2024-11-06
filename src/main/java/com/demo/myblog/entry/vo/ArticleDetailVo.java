package com.demo.myblog.entry.vo;

import com.demo.myblog.entry.table.Tag;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ArticleDetailVo {
    private String title;
    private String content;
    private String writer;
    private Integer views;
    private Date createTime;
    private Date updateTime;
    private List<String> tags;
    private Integer collects;
    private boolean collected;
}
