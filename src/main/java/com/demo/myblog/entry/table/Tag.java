package com.demo.myblog.entry.table;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("tag")
@Data
public class Tag {
    private Integer id;
    private String name;
}
