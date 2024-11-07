package com.demo.myblog.entry.vo;

import lombok.Data;

@Data
public class UserVo{
    private String nickname;
    private String avatar;
    private String email;
    private Integer sex;
    private Integer followerCount;
    private Integer followingCount;
    private Boolean isFollowing;
    private Integer id;
}
