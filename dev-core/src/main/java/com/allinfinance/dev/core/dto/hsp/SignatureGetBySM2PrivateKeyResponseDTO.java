package com.allinfinance.dev.core.dto.hsp;

import com.allinfinance.dev.core.util.validate.Check;

import java.io.Serializable;

/**
 * @author huanghf
 * @date 2022/6/19 14:46
 */
public class SignatureGetBySM2PrivateKeyResponseDTO implements Serializable {
    /**
     * 签名结果的R部分，HEX
     */
    @Check(length = 32 * 2)
    private String signatureR;
    /**
     * 签名结果的S部分，HEX
     */
    @Check(length = 32 * 2)
    private String signatureS;

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

    @Override
    public String toString() {
        return "SignatureGetBySM2PrivateKeyResponseDTO{" +
                "signatureR='" + signatureR + '\'' +
                ", signatureS='" + signatureS + '\'' +
                '}';
    }
}
