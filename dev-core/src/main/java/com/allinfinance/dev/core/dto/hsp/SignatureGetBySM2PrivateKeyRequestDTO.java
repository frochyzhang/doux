package com.allinfinance.dev.core.dto.hsp;

import com.allinfinance.dev.core.util.validate.Check;

/**
 * @author huanghf
 * @date 2022/6/19 13:54
 */
public class SignatureGetBySM2PrivateKeyRequestDTO {
    /**
     * 外部输入密钥
     */
    @Check(maxLength = 65535 / 2)
    private String externalInputKey;
    /**
     * 证书序列号
     */
    @Check(maxLength = 65535 / 2)
    private String certId;
    /**
     * 数据
     */
    @Check(maxLength = 65535 / 2)
    private String data;

    public String getExternalInputKey() {
        return externalInputKey;
    }

    public void setExternalInputKey(String externalInputKey) {
        this.externalInputKey = externalInputKey;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SignatureGetBySM2PrivateKeyRequestDTO{" +
                "externalInputKey='" + externalInputKey + '\'' +
                ", certId='" + certId + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
