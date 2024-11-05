package com.demo.myblog.entry.vo;

import lombok.Data;

@Data
public class UserVoAfterLogin {
    private String token;
    private String email;
    private String nickname;
    private String avatar;
    private String role;
    private Integer followeeCount;
    private Integer followerCount;
}
