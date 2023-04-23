package com.allinfinance.dev.rpc.scaffold.api.dto.bootstrap;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2022/3/25 10:07
 */
public class AbstractResponseDTO {
    private String responseMsg;

    public AbstractResponseDTO(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    @Override
    public String toString() {
        return "AbstractResponseDTO{" +
                "responseMsg='" + responseMsg + '\'' +
                '}';
    }
}
