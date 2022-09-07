package com.allinfinance.dev.common.api.http.dto;

import java.util.HashMap;

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
     * http调用失败原因，调用成功则不填
     */
    private String reason;
    /**
     * http header
     */
    private HashMap<String, String> header;
    /**
     * 请求体
     */
    private String body;

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

    public HashMap<String, String> getHeader() {
        return header;
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpResponseDTO{" +
                "success=" + success +
                ", reason='" + reason + '\'' +
                ", header=" + header +
                ", body='" + body + '\'' +
                '}';
    }
}
