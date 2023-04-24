package com.allinfinance.dev.common.socket.api.dto.core;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * ServResponse
 *
 * @author hongmr
 * @date 2017/6/19
 */
@XStreamAlias("SERV_RESPONSE")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "servResponse", propOrder = {
        "status",
        "code",
        "desc"
})
public class ServResponse implements Serializable {
    /**
     * STATUS: S:交易成功 F:交易失败
     */
    @XStreamAlias("STATUS")
    @XmlElement(required = true)
    private String status;
    @XStreamAlias("CODE")
    @XmlElement(required = true)
    private String code;
    @XStreamAlias("DESC")
    @XmlElement(required = true)
    private String desc;

    public ServResponse() {
    }

    public ServResponse(String status, String code, String desc) {
        this.status = status;
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "ServResponse{" +
                "status='" + status + '\'' +
                ", code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
