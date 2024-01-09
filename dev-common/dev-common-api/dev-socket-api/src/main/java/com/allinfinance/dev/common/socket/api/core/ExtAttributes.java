package com.allinfinance.dev.common.socket.api.core;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * ExtAttributes
 *
 * @author hongmr
 * @date 2017/6/19.
 */
public class ExtAttributes {
    @JacksonXmlProperty(localName = "AUTH")
    protected Auth auth;

    public Auth getAuth() {
        return auth;
    }

    public ExtAttributes setAuth(Auth auth) {
        this.auth = auth;
        return this;
    }

    @Override
    public String toString() {
        return "ExtAttributes{" +
                "auth=" + auth +
                '}';
    }
}
