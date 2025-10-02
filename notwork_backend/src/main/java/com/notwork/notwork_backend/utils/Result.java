package com.notwork.notwork_backend.utils;

import lombok.Data;

@Data
public class Result {
    private Integer code;
    private String msg;
    private Object data;

    private Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 成功场景：无消息，只有数据
    public static Result success(Object data) {
        return new Result(200, "操作成功", data);
    }

    // 成功场景：无数据，只有消息
    public static Result success(String msg) {
        return new Result(200, msg, null);
    }

    // 成功场景：无数据无消息（默认消息）
    public static Result success() {
        return new Result(200, "操作成功", null);
    }

    public static Result error(String msg) {
        return new Result(500, msg, null);
    }
    // 错误场景：支持自定义错误码
    public static Result error(int code, String msg) {
        return new Result(code, msg, null);
    }

    // 错误场景：支持携带错误数据（如校验失败的字段信息）
    public static Result error(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }
}
