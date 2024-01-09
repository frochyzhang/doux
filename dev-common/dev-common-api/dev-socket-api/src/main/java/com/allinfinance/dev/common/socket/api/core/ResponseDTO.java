package com.allinfinance.dev.common.socket.api.core;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * @author huanghf
 * @date 2024/1/8 14:56
 */
@JacksonXmlRootElement(localName = "SERVICE")
public class ResponseDTO<T> {
    @JacksonXmlProperty(isAttribute = true)
    protected String xmlns = "http://www.allinfinance.com/dataspec/";

    @JacksonXmlProperty(localName = "SERVICE_HEADER")
    protected ServiceHeaderDTO header;

    @JacksonXmlProperty(localName = "SERVICE_BODY")
    protected ResponseBodyDTO<T> responseBody;

    public String getXmlns() {
        return xmlns;
    }

    public ResponseDTO<T> setXmlns(String xmlns) {
        this.xmlns = xmlns;
        return this;
    }

    public ServiceHeaderDTO getHeader() {
        return header;
    }

    public ResponseDTO<T> setHeader(ServiceHeaderDTO header) {
        this.header = header;
        return this;
    }

    public ResponseBodyDTO<T> getResponseBody() {
        return responseBody;
    }

    public ResponseDTO<T> setResponseBody(ResponseBodyDTO<T> responseBody) {
        this.responseBody = responseBody;
        return this;
    }

    @Override
    public String toString() {
        return "ResponseDTO{" +
                "xmlns='" + xmlns + '\'' +
                ", header=" + header +
                ", responseBody=" + responseBody +
                '}';
    }
}
