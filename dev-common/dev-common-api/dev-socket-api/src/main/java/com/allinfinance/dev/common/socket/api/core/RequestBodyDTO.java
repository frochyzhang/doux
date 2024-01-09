package com.allinfinance.dev.common.socket.api.core;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/24 14:52
 */
public class RequestBodyDTO<T> {
    @JacksonXmlProperty(localName = "EXT_ATTRIBUTES")
    protected ExtAttributes extAttributes;
    @JacksonXmlProperty(localName = "REQUEST")
    protected T request;

    public ExtAttributes getExtAttributes() {
        return extAttributes;
    }

    public RequestBodyDTO<T> setExtAttributes(ExtAttributes extAttributes) {
        this.extAttributes = extAttributes;
        return this;
    }

    public T getRequest() {
        return request;
    }

    public RequestBodyDTO<T> setRequest(T request) {
        this.request = request;
        return this;
    }

    @Override
    public String toString() {
        return "RequestBodyDTO{" +
                "extAttributes=" + extAttributes +
                ", request=" + request +
                '}';
    }
}