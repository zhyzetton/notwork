package com.notwork.notwork_backend.common.enums;

import lombok.Getter;

@Getter
public enum RoleCode {

    ADMIN(0, "ROLE_ADMIN"),
    USER(1, "ROLE_USER");

    private final int code;
    private final String message;

    RoleCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static RoleCode getByCode(int code) {
        for (RoleCode role : RoleCode.values()) {
            if (role.getCode() == code) {
                return role;
            }
        }
        throw new IllegalArgumentException("无效的用户身份 code" + code);
    }
}
