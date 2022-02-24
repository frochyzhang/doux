package com.allinfinance.dev.core.dto.common;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/24 14:52
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceBodyDTO {
    @XStreamAlias("EXT_ATTRIBUTES")
    private ExtAttributes extAttributes;


    @Override
    public String toString() {
        return "ServiceBodyDTO{" +
                "extAttributes=" + extAttributes +
                '}';
    }

    public ExtAttributes getExtAttributes() {
        return extAttributes;
    }

    public void setExtAttributes(ExtAttributes extAttributes) {
        this.extAttributes = extAttributes;
    }
}
