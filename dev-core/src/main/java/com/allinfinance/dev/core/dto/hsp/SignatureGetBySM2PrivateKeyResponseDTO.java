package com.allinfinance.dev.core.dto.hsp;

import com.allinfinance.dev.core.util.validate.Check;

/**
 * @author huanghf
 * @date 2022/6/19 14:46
 */
public class SignatureGetBySM2PrivateKeyResponseDTO extends BaseResponseDTO {
    /**
     * 签名结果的R部分
     */
    @Check(length = 32 * 2)
    private String signatureR;
    /**
     * 签名结果的S部分
     */
    @Check(length = 32 * 2)
    private String signatureS;

    public SignatureGetBySM2PrivateKeyResponseDTO(Boolean success) {
        super(success);
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

    @Override
    public String toString() {
        return "SignatureGetBySM2PrivateKeyResponseDTO{" +
                "signatureR='" + signatureR + '\'' +
                ", signatureS='" + signatureS + '\'' +
                '}';
    }
}
