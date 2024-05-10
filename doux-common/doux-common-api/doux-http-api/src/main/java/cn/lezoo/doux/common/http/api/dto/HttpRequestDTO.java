package cn.lezoo.doux.common.http.api.dto;

import cn.lezoo.doux.common.http.api.constant.HttpMethod;

import java.util.Map;

/**
 * @author qipeng
 * @date 2022/9/7 10:11
 * @desc http请求客户端接收调用方的DTO
 */
public class HttpRequestDTO {
    /**
     * http请求方法
     */
    private HttpMethod httpMethod;
    /**
     * http header内容
     */
    private Map<String, String> header;
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
     * 重试次数
     */
    private Integer retryTime;
    /**
     * 超时时间，单位：秒
     */
    private Integer timeout;

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
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

    public Integer getRetryTime() {
        return retryTime;
    }

    public void setRetryTime(Integer retryTime) {
        this.retryTime = retryTime;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "HttpRequestDTO{" +
                "httpMethod=" + httpMethod +
                ", header=" + header +
                ", mediaType='" + mediaType + '\'' +
                ", body='" + body + '\'' +
                ", url='" + url + '\'' +
                ", retryTime=" + retryTime +
                ", timeout=" + timeout +
                '}';
    }
}
