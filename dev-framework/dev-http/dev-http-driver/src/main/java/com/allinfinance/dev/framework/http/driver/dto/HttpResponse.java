package com.allinfinance.dev.framework.http.driver.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qipeng
 * @date 2022/9/7 16:29
 * @desc
 */
public class HttpResponse {
    /**
     * 响应是否成功
     */
    private Boolean success;
    /**
     * 响应头
     */
    private Map<String, String> headers = new HashMap<>();
    /**
     * 响应内容
     */
    private String response;

    public HttpResponse() {
    }

    public HttpResponse(Boolean success, String response) {
        this.success = success;
        this.response = response;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpResponse setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "success=" + success +
                ", headers=" + headers +
                ", response='" + response + '\'' +
                '}';
    }
}
