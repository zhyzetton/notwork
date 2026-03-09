package com.notwork.notwork_backend.common.enums;

import lombok.Getter;

@Getter
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未登录或token已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),

    USERNAME_NOT_FOUND(1001, "用户名不存在"),
    PASSWORD_ERROR(1002, "密码错误"),
    USERNAME_ALREADY_EXISTS(1003, "用户名已存在"),

    BLOG_NOT_FOUND(2001, "博客不存在"),

    FILE_UPLOAD_ERROR(3001, "文件上传失败"),

    SYSTEM_ERROR(500, "系统内部错误");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
