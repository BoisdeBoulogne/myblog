package com.demo.myblog.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
    USER("USER"),
    ADMIN("ADMIN");
    private final String roleName;
    private RoleEnum(String roleName) {
        this.roleName = roleName;
    }
}
