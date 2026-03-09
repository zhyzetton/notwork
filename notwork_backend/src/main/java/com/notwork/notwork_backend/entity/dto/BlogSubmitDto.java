package com.notwork.notwork_backend.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BlogSubmitDto {
    @NotBlank(message = "标题不能为空")
    private String title;
    @NotBlank(message = "内容不能为空")
    private String contentMarkdown;
    private String contentHtml;
    private String coverUrl;
    private Byte status;
    @NotNull(message = "标签ID不能为空")
    private Long tagId;
}
