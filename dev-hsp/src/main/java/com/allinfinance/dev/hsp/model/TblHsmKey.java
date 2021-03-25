package com.allinfinance.dev.hsp.model;

import java.io.Serializable;

public class TblHsmKey implements Serializable {
    private Integer keyId;

    private String keyIndex;

    private String keyValue;

    private String keyKcv;

    private String zmkIndex;

    private static final long serialVersionUID = 1L;

    public Integer getKeyId() {
        return keyId;
    }

    public void setKeyId(Integer keyId) {
        this.keyId = keyId;
    }

    public String getKeyIndex() {
        return keyIndex;
    }

    public void setKeyIndex(String keyIndex) {
        this.keyIndex = keyIndex;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getKeyKcv() {
        return keyKcv;
    }

    public void setKeyKcv(String keyKcv) {
        this.keyKcv = keyKcv;
    }

    public String getZmkIndex() {
        return zmkIndex;
    }

    public void setZmkIndex(String zmkIndex) {
        this.zmkIndex = zmkIndex;
    }
}