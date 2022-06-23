package com.allinfinance.dev.core.dto.hsp;

/**
 * @author huanghf
 * @date 2022/6/22 17:13
 */
public class BaseResponseDTO {
    /**
     * 是否成功
     */
    protected Boolean success;

    public BaseResponseDTO(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "BaseResponseDTO{" +
                "success=" + success +
                '}';
    }
}
