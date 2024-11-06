package com.demo.myblog.entry.table;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@TableName("history")
@AllArgsConstructor
public class History {
    private Date date;
    private Integer userId;
    private Integer articleId;
}
