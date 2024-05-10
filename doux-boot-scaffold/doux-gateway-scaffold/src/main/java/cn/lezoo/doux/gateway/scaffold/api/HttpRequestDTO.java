package cn.lezoo.doux.gateway.scaffold.api;

import cn.lezoo.doux.gateway.scaffold.processor.dto.AbstractRequestDTO;
import io.netty.handler.codec.http.HttpMethod;

import java.util.Map;

/**
 * @author huanghf
 * @date 2023/12/12 15:00
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
