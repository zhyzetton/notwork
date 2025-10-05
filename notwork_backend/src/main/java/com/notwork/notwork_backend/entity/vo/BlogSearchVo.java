package com.notwork.notwork_backend.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogSearchVo {

    private Long id;
    private String title;
    private String coverUrl;
    private Byte status;
    private Integer viewCount;
    private Integer likeCount;
    private Integer collectCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 用户信息
    private Long userId;
    private String username;
    private String userAvatar;

    // 标签信息
    private String tagName;
}
