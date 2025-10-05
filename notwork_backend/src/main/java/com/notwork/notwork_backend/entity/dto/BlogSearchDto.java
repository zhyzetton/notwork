package com.notwork.notwork_backend.entity.dto;

import lombok.Data;

@Data
public class BlogSearchDto {
    private Integer pageNum;
    private Integer pageSize;
    private String title;
    private Long tagId;
    private String tagCode;
    private Long userId;
    private Byte status;
}
