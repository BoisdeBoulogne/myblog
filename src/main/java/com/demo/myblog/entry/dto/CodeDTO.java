package com.demo.myblog.entry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
@Data
@AllArgsConstructor
public class CodeDTO {
    private Integer code;
    private Date date;
}
