package com.allinfinance.dev.common.socket.api.dto.core;


import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

/**
 * ExtAttributes
 *
 * @author hongmr
 * @date 2017/6/19.
 * @deprecated 请使用 {@link com.allinfinance.dev.common.socket.api.core.ExtAttributes}
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ExtAttributes implements Serializable {
    @XStreamAlias("AUTH")
    protected Auth auth;

    public ExtAttributes() {
    }

    public ExtAttributes(Auth auth) {
        this.auth = auth;
    }

    @Override
    public String toString() {
        return "ExtAttributes{" +
                "auth=" + auth +
                '}';
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }
}
