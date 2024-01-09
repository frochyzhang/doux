package com.allinfinance.dev.rpc.scaffold.advice.dto;

import com.allinfinance.dev.common.dictionary.advice.ResponseCode;

/**
 * 默认rpc响应码
 *
 * @author huanghf
 */
public enum BaseResponseCodeEnum implements ResponseCode {
    SUCCESS("00000000", "成功"),
    FAIL("99999999", "失败");

    private final String code;
    private final String message;

    BaseResponseCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
