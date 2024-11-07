package com.demo.myblog.entry.vo;

import lombok.Data;

import java.util.List;
@Data
public class PageVo {
    private Integer articleCount;
    private List<ArticlePerVo> articles;

}
