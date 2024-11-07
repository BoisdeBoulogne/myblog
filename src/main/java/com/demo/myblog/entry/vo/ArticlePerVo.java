package com.demo.myblog.entry.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ArticlePerVo {
    private String avatar;
    private String title;
    private String contentPre;
    private Date createTime;
    private Date updateTime;
    private String writer;
    private Integer views;
    private List<String> tags;
    private Integer collects;
    private Integer id;
}
