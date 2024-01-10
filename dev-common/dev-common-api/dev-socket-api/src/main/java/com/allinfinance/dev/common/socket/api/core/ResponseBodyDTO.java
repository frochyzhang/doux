package com.allinfinance.dev.common.socket.api.core;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/24 14:52
 */
public class ResponseBodyDTO<T> {
    @JacksonXmlProperty(localName = "EXT_ATTRIBUTES")
    protected ExtAttributes extAttributes;
    @JacksonXmlProperty(localName = "RESPONSE")
    protected T response;

    public ExtAttributes getExtAttributes() {
        return extAttributes;
    }

    public ResponseBodyDTO<T> setExtAttributes(ExtAttributes extAttributes) {
        this.extAttributes = extAttributes;
        return this;
    }

    public T getResponse() {
        return response;
    }

    public ResponseBodyDTO<T> setResponse(T response) {
        this.response = response;
        return this;
    }

    @Override
    public String toString() {
        return "ResponseBodyDTO{" +
                "extAttributes=" + extAttributes +
                ", response=" + response +
                '}';
    }
}
