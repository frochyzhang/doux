package com.allinfinance.dev.framework.http.driver.dto;

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
                ", response='" + response + '\'' +
                '}';
    }
}
