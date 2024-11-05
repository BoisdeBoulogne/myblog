package com.demo.myblog.enums;

import lombok.Data;
import lombok.Getter;

@Getter

public enum AppEnum {
    SUCCESS(200,"操作成功"),
    NULL_CODE(502,"先获取验证码"),
    WRONG_CODE(506,"验证码错误或为空"),
    ERROR_AUTH(506,"账户或密码错误"),
    FORCE_LOGOUT(401,"账号被强制登出了"),
    EXIST_DATA(504,"被占用的值"),
    WRONG_EMAIL(505,"邮箱地址错误"),
    WRONG_QUERY(506,"访问参数错误"),
    WRONG_JWT(401,"jwt解析错误"),
    EXIST_CODE(501,"已经发送过，请稍后重试");

    private final int code;
    private final String msg;
    private AppEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
