package com.notwork.notwork_backend.entity.dto;

import lombok.Data;

@Data
public class BlogSearchDto {
    private Integer pageNum;
    private Integer pageSize;
    private String title;
    private Long tagId;
    private Long userId;
    private Byte status;
}
