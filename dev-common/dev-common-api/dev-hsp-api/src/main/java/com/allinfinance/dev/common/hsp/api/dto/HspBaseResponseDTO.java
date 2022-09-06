package com.allinfinance.dev.common.hsp.api.dto;

import java.io.Serializable;

/**
 * @author huanghf
 * @date 2022/6/22 17:13
 */
public class HspBaseResponseDTO<T> implements Serializable {
    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 描述
     */
    private String desc;
    /**
     * 响应数据
     */
    private T response;

    public HspBaseResponseDTO() {
    }

    public HspBaseResponseDTO(Boolean success, String desc, T response) {
        this.success = success;
        this.desc = desc;
        this.response = response;
    }

    public HspBaseResponseDTO(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "HspBaseResponseDTO{" +
                "success=" + success +
                ", desc='" + desc + '\'' +
                ", response=" + response +
                '}';
    }

    public static <T> HspBaseResponseDTO<T> success(String msg, T response) {
        return new HspBaseResponseDTO<>(Boolean.TRUE, msg, response);
    }

    public static <T> HspBaseResponseDTO<T> fail(String msg) {
        return new HspBaseResponseDTO<>(Boolean.FALSE, msg, null);
    }
}
