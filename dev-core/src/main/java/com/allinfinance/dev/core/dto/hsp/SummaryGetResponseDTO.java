package com.allinfinance.dev.core.dto.hsp;

import com.allinfinance.dev.core.util.validate.Check;

/**
 * @author huanghf
 * @date 2022/6/21 14:30
 */
public class SummaryGetResponseDTO extends BaseResponseDTO {
    @Check(maxLength = 65535 * 2)
    private String summaryData;

    public SummaryGetResponseDTO(Boolean success) {
        super(success);
    }

    public String getSummaryData() {
        return summaryData;
    }

    public void setSummaryData(String summaryData) {
        this.summaryData = summaryData;
    }

    @Override
    public String toString() {
        return "SummaryGetResponseDTO{" +
                "summaryData='" + summaryData + '\'' +
                '}';
    }
}
