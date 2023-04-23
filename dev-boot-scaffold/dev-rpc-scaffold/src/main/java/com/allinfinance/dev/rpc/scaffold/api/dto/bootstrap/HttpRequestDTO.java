package com.allinfinance.dev.rpc.scaffold.api.dto.bootstrap;

import com.allinfinance.dev.common.api.http.constant.HttpMethod;

import java.util.Map;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2022/3/15 9:45
 */
public class HttpRequestDTO extends AbstractRequestDTO {
    private HttpMethod httpMethod;

    private String url;

    private Map<String, String> headers;

    private Map<String, String> params;

    public HttpRequestDTO(String requestMsg) {
        super(requestMsg);
    }

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

    public String getHeader(String headerKey) {
        return this.headers.get(headerKey);
    }

    public String getParam(String paramKey) {
        return this.params.get(paramKey);
    }

    @Override
    public String toString() {
        return "HttpRequestDTO{" +
                "httpMethod=" + httpMethod +
                ", url='" + url + '\'' +
                ", headers=" + headers +
                ", params=" + params +
                "} " + super.toString();
    }
}
