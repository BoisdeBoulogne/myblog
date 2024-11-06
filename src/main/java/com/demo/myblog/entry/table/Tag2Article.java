package com.demo.myblog.entry.table;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("tag2article")
@Data
public class Tag2Article {
    private Integer tagId;
    private Integer articleId;
}

