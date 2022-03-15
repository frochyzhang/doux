package com.allinfinance.dev.rpc.scaffold.api.dto;

import org.springframework.http.HttpMethod;

import java.util.Map;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2022/3/15 9:45
 */
public class HttpRequestDTO {
    private HttpMethod httpMethod;

    private String url;

    private String body;

    private Map<String, String> headers;

    private Map<String, String> params;

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "HttpRequestDTO{" +
                "httpMethod=" + httpMethod +
                ", url='" + url + '\'' +
                ", body='" + body + '\'' +
                ", headers=" + headers +
                ", params=" + params +
                '}';
    }
}
