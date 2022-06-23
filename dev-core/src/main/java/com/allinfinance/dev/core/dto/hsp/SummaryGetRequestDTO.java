package com.allinfinance.dev.core.dto.hsp;

import com.allinfinance.dev.core.dto.hsp.constant.HashAlgorithmEnum;
import com.allinfinance.dev.core.util.validate.Check;

/**
 * @author huanghf
 * @date 2022/6/21 14:30
 */
public class SummaryGetRequestDTO {
    /**
     * 摘要算法
     */
    @Check
    private HashAlgorithmEnum hashAlgorithm;
    /**
     * 数据
     */
    @Check(maxLength = 65535 / 2)
    private String data;

    public HashAlgorithmEnum getHashAlgorithm() {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(HashAlgorithmEnum hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SummaryGetRequestDTO{" +
                "hashAlgorithm='" + hashAlgorithm + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
