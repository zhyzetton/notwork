package com.notwork.notwork_backend.entity.vo;

import lombok.Data;

@Data
public class LoginVo {
    private String token;
    private Long userId;
    private String username;
    private String avatarUrl;
    private Byte role;
}
