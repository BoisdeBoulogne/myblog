package com.demo.myblog.entry.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HomePageVo {
    private PageVo page;
    private Integer userCount;
}
