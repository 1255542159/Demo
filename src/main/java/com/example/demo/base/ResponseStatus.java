package com.example.demo.base;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 返回状态码
 * @author joy
 * @version 1.0
 * @date 2020/12/28 16:25
 */

public enum ResponseStatus {
    SUCCESS(200, "获取成功"),
    AUTHENTICATION_FAILURE(403, "授权失败"),
    FAILURE(400, "获取失败"),
    NOT_FOUND(404, "页面丢失"),
    INTERNAL_SERVER_ERROR(505, "服务器内部错误");

    private boolean success;
    private int code;
    private String message;

    ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
