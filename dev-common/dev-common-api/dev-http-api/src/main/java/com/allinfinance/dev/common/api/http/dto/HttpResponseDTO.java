package com.allinfinance.dev.common.api.http.dto;

/**
 * @author qipeng
 * @date 2022/9/7 10:11
 * @desc http客户端返回给调用方DTO
 */
public class HttpResponseDTO {
    /**
     * http调用是否成功
     */
    private Boolean success;
    /**
     * 响应内容
     */
    private String response;

    public HttpResponseDTO(Boolean success, String response) {
        this.success = success;
        this.response = response;
    }

    public HttpResponseDTO() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getBody() {
        return response;
    }

    public void setBody(String body) {
        this.response = body;
    }

    @Override
    public String toString() {
        return "HttpResponseDTO{" +
                "success=" + success +
                ", response='" + response + '\'' +
                '}';
    }
}
