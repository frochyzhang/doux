package com.allinfinance.dev.common.transmit.api.dto;

/**
 * @author qipeng
 * @date 2022/9/9 15:29
 * @desc 文件传输结果
 */
public class TransmitResponseDTO {
    /**
     * 传输是否成功
     */
    private Boolean success;
    /**
     * 传输失败原因，成功则不填
     */
    private String reason;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "TransmitResponseDTO{" +
                "success=" + success +
                ", reason='" + reason + '\'' +
                '}';
    }
}
