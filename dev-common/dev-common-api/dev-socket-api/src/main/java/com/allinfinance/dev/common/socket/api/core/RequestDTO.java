package com.allinfinance.dev.common.socket.api.core;

import cn.hutool.core.date.DateUtil;
import com.allinfinance.dev.common.util.common.DateUtils;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;

/**
 * CommonTransDto
 *
 * @author hongmr
 * @date 2017/6/19.
 */
@JacksonXmlRootElement(localName = "SERVICE")
public class RequestDTO<T> {
    @JacksonXmlProperty(isAttribute = true)
    protected String xmlns = "http://www.allinfinance.com/dataspec/";

    @JacksonXmlProperty(localName = "SERVICE_HEADER")
    protected ServiceHeaderDTO header;

    @JacksonXmlProperty(localName = "SERVICE_BODY")
    protected RequestBodyDTO<T> requestBody;

    public String getXmlns() {
        return xmlns;
    }

    public RequestDTO<T> setXmlns(String xmlns) {
        this.xmlns = xmlns;
        return this;
    }

    public ServiceHeaderDTO getHeader() {
        return header;
    }

    public RequestDTO<T> setHeader(ServiceHeaderDTO header) {
        this.header = header;
        return this;
    }

    public RequestBodyDTO<T> getRequestBody() {
        return requestBody;
    }

    public RequestDTO<T> setRequestBody(RequestBodyDTO<T> requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    @Override
    public String toString() {
        return "RequestDTO{" +
                "xmlns='" + xmlns + '\'' +
                ", header=" + header +
                ", requestBody=" + requestBody +
                '}';
    }

    public ResponseDTO<?> createResp(NFResponseCode responseCode) {
        return new ResponseDTO<>()
                .setHeader(
                        this.header
                                .setResServiceSn(getServiceSn())
                                .setResServiceTime(DateUtils.getCurrentDateTime())
                                .setServResponse(
                                        new ServResponse()
                                                .setStatus(responseCode.getStatus())
                                                .setCode(responseCode.getCode())
                                                .setDesc(responseCode.getDesc())
                                )
                )
                .setXmlns(this.xmlns);
    }

    public ResponseDTO<?> createResp(String status, String code, String desc) {
        return new ResponseDTO<>()
                .setHeader(
                        this.header
                                .setResServiceSn(getServiceSn())
                                .setResServiceTime(DateUtils.getCurrentDateTime())
                                .setServResponse(
                                        new ServResponse()
                                                .setStatus(status)
                                                .setCode(code)
                                                .setDesc(desc)
                                )
                )
                .setXmlns(this.xmlns);
    }

    public <S> ResponseDTO<S> createResp(S response) {
        return new ResponseDTO<S>()
                .setXmlns(this.xmlns)
                .setHeader(
                        this.header
                                .setResServiceSn(getServiceSn())
                                .setResServiceTime(DateUtils.getCurrentDateTime())
                                .setServResponse(
                                        new ServResponse()
                                                .setStatus(BaseNFResponseCode.SUCCESS.getStatus())
                                                .setCode(BaseNFResponseCode.SUCCESS.getCode())
                                                .setDesc(BaseNFResponseCode.SUCCESS.getDesc())
                                )
                )
                .setResponseBody(
                        new ResponseBodyDTO<S>()
                                .setResponse(response)
                );
    }

    private String getServiceSn() {
        return DateUtil.format(new Date(), "yyyyMMddHHmmss") + RandomStringUtils.randomNumeric(5);
    }
}
