package com.allinfinance.dev.core.dto.common;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * CommonTransDto
 *
 * @author hongmr
 * @date 2017/6/19.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceDTO {
    @XStreamAlias("SERVICE_HEADER")
    protected ServiceHeaderDTO header;

    @XStreamAsAttribute
    protected String xmlns = "http://www.allinfinance.com/dataspec/";

    @Override
    public String toString() {
        return "ServiceDTO{" +
                "header=" + header +
                ", xmlns='" + xmlns + '\'' +
                '}';
    }

    public void setProcRes(String code, String desc) {
        ServiceHeaderDTO bankHeader = this.header;
        ServResponse servResponse = new ServResponse();
        if ("SSSS".equals(code)
                || "S000".equals(code)) {
            servResponse.setStatus("S");
        } else {
            servResponse.setStatus("F");
        }
        servResponse.setCode(code);
        servResponse.setDesc(desc);
        bankHeader.setServResponse(servResponse);
        this.header = bankHeader;
    }

    public ServiceDTO createResp(String sysSn, String sysTime) {
        ServiceDTO resp = null;
        String respClazzName = null;
        try {
            String selfClazzName = this.getClass().getName();
            if (StringUtils.isNotEmpty(selfClazzName) && selfClazzName.endsWith("ReqDto")) {
                respClazzName = selfClazzName.substring(0, selfClazzName.length() - 6) + "RespDto";
            } else {
                respClazzName = selfClazzName;
            }
            resp = (ServiceDTO) Class.forName(respClazzName).newInstance();
            ServiceHeaderDTO bankHeader = this.header;
            bankHeader.setResServiceSn(sysSn);
            bankHeader.setResServiceTime(sysTime);
            resp.setHeader(bankHeader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }


    public <T extends ServiceDTO> T createResp(Class<T> tClass, String sysSn, String sysTime) {
        T resp = null;
        try {
            String selfClazzName = this.getClass().getName();
            resp = tClass.newInstance();
            ServiceHeaderDTO bankHeader = this.header;
            bankHeader.setResServiceSn(sysSn);
            bankHeader.setResServiceTime(sysTime);
            resp.setHeader(bankHeader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }


    public ServiceHeaderDTO getHeader() {
        return header;
    }

    public void setHeader(ServiceHeaderDTO header) {
        this.header = header;
    }
}
