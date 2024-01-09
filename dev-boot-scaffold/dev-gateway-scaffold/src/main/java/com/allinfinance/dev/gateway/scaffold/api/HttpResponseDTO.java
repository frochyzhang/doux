package com.allinfinance.dev.gateway.scaffold.api;

import com.allinfinance.dev.gateway.scaffold.processor.dto.AbstractResponseDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huanghf
 * @date 2023/12/12 15:01
 */
public class HttpResponseDTO extends AbstractResponseDTO {
    private Map<String, String> headers = new ConcurrentHashMap<>();

    public HttpResponseDTO(String responseMsg) {
        super(responseMsg);
    }

    public void fillHeader(@NonNull String key, @NonNull String value) {
        if (StringUtils.isNotEmpty(value)) {
            this.headers.put(key, value);
        }
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
