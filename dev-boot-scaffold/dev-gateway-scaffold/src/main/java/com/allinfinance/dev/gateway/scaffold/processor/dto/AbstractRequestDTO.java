package com.allinfinance.dev.gateway.scaffold.processor.dto;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2022/3/15 11:14
 */
public class AbstractRequestDTO {
    private String requestMsg;

    public AbstractRequestDTO(String requestMsg) {
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
        return "AbstractRequestDTO{" +
                "requestMsg='" + requestMsg + '\'' +
                '}';
    }

}
