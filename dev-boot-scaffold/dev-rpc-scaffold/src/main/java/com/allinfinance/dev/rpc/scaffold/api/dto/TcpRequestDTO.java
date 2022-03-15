package com.allinfinance.dev.rpc.scaffold.api.dto;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2022/3/15 9:45
 */
public class TcpRequestDTO {
    private String requestMsg;

    public TcpRequestDTO(String requestMsg) {
        this.requestMsg = requestMsg;
    }

    public String getRequestMsg() {
        return requestMsg;
    }

    public void setRequestMsg(String requestMsg) {
        this.requestMsg = requestMsg;
    }

    @Override
    public String toString() {
        return "TcpRequestDTO{" +
                "requestMsg='" + requestMsg + '\'' +
                '}';
    }
}
