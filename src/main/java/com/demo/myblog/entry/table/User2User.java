package com.demo.myblog.entry.table;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user2user")
public class User2User {
    private Integer followeeId;
    private Integer followerId;
}
