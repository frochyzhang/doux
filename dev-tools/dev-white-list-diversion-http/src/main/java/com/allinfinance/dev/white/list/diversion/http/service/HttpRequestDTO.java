package com.allinfinance.dev.white.list.diversion.http.service;

import java.util.Map;

/**
 * @author huanghf
 * @date 2024/3/20 22:59
 */
public class HttpRequestDTO {
    private final String uri;
    private final Map<String, String> headers;
    private final String body;
    private final String method;

    public HttpRequestDTO(String uri, Map<String, String> headers, String body, String method) {
        this.uri = uri;
        this.headers = headers;
        this.body = body;
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "HttpRequestDTO{" +
                "uri='" + uri + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
