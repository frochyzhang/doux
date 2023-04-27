package com.allinfinance.dev.common.dictionary.processor.dto;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2022/3/15 9:44
 */
public class ProcessRequestDTO {
    private RequestTypeEnum requestType;

    private AbstractRequestDTO requestDTO;

    public ProcessRequestDTO(RequestTypeEnum requestType) {
        this.requestType = requestType;
    }

    public RequestTypeEnum getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestTypeEnum requestType) {
        this.requestType = requestType;
    }

    public AbstractRequestDTO getRequestDTO() {
        return requestDTO;
    }

    public void setRequestDTO(AbstractRequestDTO requestDTO) {
        this.requestDTO = requestDTO;
    }

    @Override
    public String toString() {
        return "ProcessRequestDTO{" +
                "requestType=" + requestType +
                ", requestDTO=" + requestDTO +
                '}';
    }
}
