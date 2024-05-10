package cn.lezoo.doux.white.list.diversion.http.backup;

import java.util.Map;

/**
 * @author huanghf
 * @date 2024/3/26 16:29
 */
public class MessageInfo {
    private String method;
    private String url;
    private Map<String, String> header;
    private String data;

    public String getMethod() {
        return method;
    }

    public MessageInfo setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public MessageInfo setUrl(String url) {
        this.url = url;
        return this;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public MessageInfo setHeader(Map<String, String> header) {
        this.header = header;
        return this;
    }

    public String getData() {
        return data;
    }

    public MessageInfo setData(String data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", header=" + header +
                ", data='" + data + '\'' +
                '}';
    }
}
