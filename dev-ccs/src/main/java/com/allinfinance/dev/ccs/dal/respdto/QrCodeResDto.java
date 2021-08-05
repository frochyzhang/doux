package com.allinfinance.dev.ccs.dal.respdto;

import java.io.Serializable;

public class QrCodeResDto implements Serializable {
    private String qrCode;
    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
