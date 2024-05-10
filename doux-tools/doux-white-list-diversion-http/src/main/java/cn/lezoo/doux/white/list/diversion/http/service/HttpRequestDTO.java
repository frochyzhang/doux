package cn.lezoo.doux.white.list.diversion.http.service;

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
    private final Map<String, String> parameters;

    public HttpRequestDTO(String uri, Map<String, String> headers, String body, String method, Map<String, String> parameters) {
        this.uri = uri;
        this.headers = headers;
        this.body = body;
        this.method = method;
        this.parameters = parameters;
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

    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "HttpRequestDTO{" +
                "uri='" + uri + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                ", method='" + method + '\'' +
                ", parameters=" + parameters +
                '}';
    }

    public String getHeader(String name) {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(name)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }
}
