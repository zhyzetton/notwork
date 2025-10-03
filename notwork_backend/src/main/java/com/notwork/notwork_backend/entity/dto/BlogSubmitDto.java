package com.notwork.notwork_backend.entity.dto;

import lombok.Data;

@Data
public class BlogSubmitDto {
    private Long userId;
    private String title;
    private String contentMarkdown;
    private String coverUrl;
    private Byte status;
    private Long tagId;
}
