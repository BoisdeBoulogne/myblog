package com.demo.myblog.entry.table;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@TableName("user")
@Data
public class User {
    @TableId
    private Integer id;

    private String username;

    private String nickname;

    private String password;

    private String email;

    private int sex;

    private String avatar;

    //true为未封禁
    private boolean unlocked;

    private String role;
}
