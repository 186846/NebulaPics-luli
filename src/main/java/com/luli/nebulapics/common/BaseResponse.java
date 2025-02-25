package com.luli.nebulapics.common;

import com.luli.nebulapics.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 基础响应定义
 * @param <T>
 */

@Data
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data;
    private String msg;

    public BaseResponse(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(),null,errorCode.getMessage());
    }
}
