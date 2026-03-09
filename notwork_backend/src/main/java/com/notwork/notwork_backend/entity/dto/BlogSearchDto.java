package com.notwork.notwork_backend.entity.dto;

import com.notwork.notwork_backend.common.constants.CommonConstants;
import lombok.Data;

@Data
public class BlogSearchDto {
    private Integer pageNum = CommonConstants.DEFAULT_PAGE_NUM;
    private Integer pageSize = CommonConstants.DEFAULT_PAGE_SIZE;
    private String title;
    private Long tagId;
    private String tagCode;
    private Long userId;
    private Byte status;
}
