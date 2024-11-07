package com.demo.myblog.entry.dto;

import com.demo.myblog.constant.Constants;
import com.demo.myblog.enums.RoleEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeRoleDTO {
    @NotNull(message = "用户名不能为空")
    private Integer userId;
    @NotNull(message = "修改的权限不能为空")
    private RoleEnum roleName;
}
