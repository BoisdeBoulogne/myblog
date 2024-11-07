package com.demo.myblog.entry.vo;

import lombok.Data;

import java.util.List;
@Data
public class PageVo<T> {
    private Integer count;
    private List<T> entry;

}
