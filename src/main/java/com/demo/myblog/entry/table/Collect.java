package com.demo.myblog.entry.table;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@TableName("collect")
@Data
@AllArgsConstructor
public class Collect {
    private Integer userId;
    private Integer articleId;
}
