package com.allinfinance.dev.common.socket.client.dto;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/08 10:40
 */
public class SocketResponseDTO {
    /**
     * Socket调用是否成功
     */
    private Boolean success;
    /**
     * 响应内容
     */
    private String response;

    public SocketResponseDTO() {
    }

    public SocketResponseDTO(Boolean success, String response) {
        this.success = success;
        this.response = response;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "SocketResponseDTO{" +
                "success=" + success +
                ", response='" + response + '\'' +
                '}';
    }
}
