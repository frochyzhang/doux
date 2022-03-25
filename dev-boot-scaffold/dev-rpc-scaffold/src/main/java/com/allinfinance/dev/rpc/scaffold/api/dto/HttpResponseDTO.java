package com.allinfinance.dev.rpc.scaffold.api.dto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2022/3/15 9:45
 */
public class HttpResponseDTO extends AbstractResponseDTO {
    private Map<String, String> headers = new ConcurrentHashMap<>();

    public HttpResponseDTO(String responseMsg) {
        super(responseMsg);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getHeader(String headerKey) {
        return this.headers.get(headerKey);
    }

    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    @Override
    public String toString() {
        return "HttpResponseDTO{" +
                "headers=" + headers +
                "} " + super.toString();
    }
}
