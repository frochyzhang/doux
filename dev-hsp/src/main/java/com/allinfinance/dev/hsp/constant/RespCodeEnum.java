package com.allinfinance.dev.hsp.constant;

/**
 * @author huanghf
 * @date 2022/6/23 10:43
 */
public enum RespCodeEnum {
    SUCCESS("41", "成功"),
    FAIL("45", "失败");
    private final String respCode;
    private final String respDesc;

    RespCodeEnum(String respCode, String respDesc) {
        this.respCode = respCode;
        this.respDesc = respDesc;
    }

    public String getRespCode() {
        return respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }
}
