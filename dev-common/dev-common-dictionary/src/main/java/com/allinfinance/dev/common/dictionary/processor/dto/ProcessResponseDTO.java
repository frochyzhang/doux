package com.allinfinance.dev.common.dictionary.processor.dto;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2022/3/25 10:05
 */
public class ProcessResponseDTO {
    private RequestTypeEnum responseType;
    private AbstractResponseDTO responseDTO;

    public ProcessResponseDTO(RequestTypeEnum responseType) {
        this.responseType = responseType;
    }

    public RequestTypeEnum getResponseType() {
        return responseType;
    }

    public void setResponseType(RequestTypeEnum responseType) {
        this.responseType = responseType;
    }

    public AbstractResponseDTO getResponseDTO() {
        return responseDTO;
    }

    public void setResponseDTO(AbstractResponseDTO responseDTO) {
        this.responseDTO = responseDTO;
    }

    @Override
    public String toString() {
        return "ProcessResponseDTO{" +
                "responseType=" + responseType +
                ", responseDTO=" + responseDTO +
                '}';
    }
}
