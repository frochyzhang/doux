package com.allinfinance.dev.common.socket.api.core;

/**
 * @author huanghf
 * @date 2024/1/8 16:46
 */
public enum BaseNFResponseCode implements NFResponseCode {
    SUCCESS("S", "SSSS", "交易成功"),
    SYSTEM_ERROR("F", "S001", "系统处理异常");

    private final String status;
    private final String code;
    private final String desc;

    BaseNFResponseCode(String status, String code, String desc) {
        this.status = status;
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
