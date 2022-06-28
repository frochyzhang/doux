package com.allinfinance.dev.core.dto.hsp;

import com.allinfinance.dev.core.util.validate.Check;

/**
 * @author huanghf
 * @date 2022/6/21 16:33
 */
public class SignatureVerifyBySM2PublicKeyRequestDTO {
    /**
     * 公钥明文X, HEX
     */
    @Check(length = 32 * 2)
    private String plainPublicKeyX;
    /**
     * 公钥明文Y, HEX
     */
    @Check(length = 32 * 2)
    private String plainPublicKeyY;
    /**
     * 签名结果R, HEX
     */
    @Check(length = 32 * 2)
    private String signatureR;
    /**
     * 签名结果S, HEX
     */
    @Check(length = 32 * 2)
    private String signatureS;
    /**
     * 证书序列号，明文
     */
    @Check(minLength = 1, maxLength = 65535 / 2)
    private String certId;
    /**
     * 数据，明文
     */
    @Check(minLength = 1, maxLength = 65535 / 2)
    private String data;

    public String getPlainPublicKeyX() {
        return plainPublicKeyX;
    }

    public void setPlainPublicKeyX(String plainPublicKeyX) {
        this.plainPublicKeyX = plainPublicKeyX;
    }

    public String getPlainPublicKeyY() {
        return plainPublicKeyY;
    }

    public void setPlainPublicKeyY(String plainPublicKeyY) {
        this.plainPublicKeyY = plainPublicKeyY;
    }

    public String getSignatureR() {
        return signatureR;
    }

    public void setSignatureR(String signatureR) {
        this.signatureR = signatureR;
    }

    public String getSignatureS() {
        return signatureS;
    }

    public void setSignatureS(String signatureS) {
        this.signatureS = signatureS;
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
        return "SignatureVerifyBySM2PublicKeyRequestDTO{" +
                "plainPublicKeyX='" + plainPublicKeyX + '\'' +
                ", plainPublicKeyY='" + plainPublicKeyY + '\'' +
                ", signatureR='" + signatureR + '\'' +
                ", signatureS='" + signatureS + '\'' +
                ", certId='" + certId + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
