package com.notwork.notwork_backend.entity.dto;

import lombok.Data;

@Data
public class ChatWithRagDto {
    private Long userId;
    private String query;
}
