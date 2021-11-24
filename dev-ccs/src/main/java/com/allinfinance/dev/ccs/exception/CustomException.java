package com.allinfinance.dev.ccs.exception;

import com.allinfinance.dev.ccs.result.ResultCodeEnum;

/**
 * @author 张勇
 * @description
 * @date 2020/5/17 02:24
 */
public class CustomException extends RuntimeException {
    private String code;
    private String message;

    public CustomException(String code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.message());
        this.code = resultCodeEnum.code();
        this.message = resultCodeEnum.message();
    }

    @Override
    public String toString() {
        return "CustomException{" + "code=" + code + ", message=" + this.getMessage() + '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
