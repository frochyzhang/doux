package com.allinfinance.dev.rpc.scaffold.api.dto;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2022/3/15 9:44
 */
public class ProcessRequestDTO {
    private RequestTypeEnum requestType;

    private TcpRequestDTO tcpRequest;

    private HttpRequestDTO httpRequest;

    public ProcessRequestDTO(RequestTypeEnum requestType) {
        this.requestType = requestType;
    }

    public RequestTypeEnum getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestTypeEnum requestType) {
        this.requestType = requestType;
    }

    public TcpRequestDTO getTcpRequest() {
        return tcpRequest;
    }

    public void setTcpRequest(TcpRequestDTO tcpRequest) {
        this.tcpRequest = tcpRequest;
    }

    public HttpRequestDTO getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(HttpRequestDTO httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Override
    public String toString() {
        return "ProcessRequestDTO{" +
                "requestType=" + requestType +
                ", tcpRequest=" + tcpRequest +
                ", httpRequest=" + httpRequest +
                '}';
    }
}
