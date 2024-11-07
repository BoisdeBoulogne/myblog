package com.demo.myblog.entry.dto;

import com.demo.myblog.constant.Constants;
import com.demo.myblog.enums.RoleEnum;
import lombok.Data;

@Data
public class ChangeRoleDTO {
    private Integer userId;
    private RoleEnum roleName;
}
