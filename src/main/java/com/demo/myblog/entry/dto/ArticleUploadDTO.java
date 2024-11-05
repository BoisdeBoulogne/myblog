package com.demo.myblog.entry.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ArticleUploadDTO {
    @NotNull( message = "标题不能为空")
    private String title;
    private String avatar;
    @NotNull(message = "内容不能为空")
    private String content;
}
