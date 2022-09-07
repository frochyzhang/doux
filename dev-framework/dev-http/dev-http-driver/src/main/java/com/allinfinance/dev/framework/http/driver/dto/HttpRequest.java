package com.allinfinance.dev.framework.http.driver.dto;

import java.util.HashMap;

/**
 * @author qipeng
 * @date 2022/9/7 16:25
 * @desc
 */
public class HttpRequest {
    /**
     * http请求方法，参数需大写
     */
    private String httpMethod;
    /**
     * http header内容
     */
    private HashMap<String, String> header;
    /**
     * mediaType，调用方提供string，实现方自行解析
     */
    private String mediaType;
    /**
     * 请求体
     */
    private String body;
    /**
     * 请求路径
     */
    private String url;
    /**
     * 超时时间，单位：秒
     */
    private Integer timeout;

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "httpMethod='" + httpMethod + '\'' +
                ", header=" + header +
                ", mediaType='" + mediaType + '\'' +
                ", body='" + body + '\'' +
                ", url='" + url + '\'' +
                ", timeout=" + timeout +
                '}';
    }
}
