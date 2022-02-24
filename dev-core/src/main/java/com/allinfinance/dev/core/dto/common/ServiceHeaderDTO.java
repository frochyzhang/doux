package com.allinfinance.dev.core.dto.common;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * CommonHeaderDto
 *
 * @author hongmr
 * @date 2017/6/19.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceHeaderDTO {
    @XStreamAlias("SERVICE_SN")
    protected String serviceSn;
    @XStreamAlias("SERVICE_ID")
    protected String serviceId;
    @XStreamAlias("ORG")
    protected String org;
    @XStreamAlias("CHANNEL_ID")
    protected String channelId;
    @XStreamAlias("OP_ID")
    protected String opId;
    @XStreamAlias("REQUST_TIME")
    protected String requstTime;
    @XStreamAlias("VERSION_ID")
    protected String versionId;
    @XStreamAlias("MAC")
    protected String mac;
    @XStreamAlias("RES_SERVICE_SN")
    protected String resServiceSn;
    @XStreamAlias("RES_SERVICE_TIME")
    protected String resServiceTime;
    @XStreamAlias("SERV_RESPONSE")
    protected ServResponse servResponse;

    @Override
    public String toString() {
        return "ServiceHeaderDTO{" +
                "serviceSn='" + serviceSn + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", org='" + org + '\'' +
                ", channelId='" + channelId + '\'' +
                ", opId='" + opId + '\'' +
                ", requstTime='" + requstTime + '\'' +
                ", versionId='" + versionId + '\'' +
                ", mac='" + mac + '\'' +
                ", resServiceSn='" + resServiceSn + '\'' +
                ", resServiceTime='" + resServiceTime + '\'' +
                ", servResponse=" + servResponse +
                '}';
    }

    public String getServiceSn() {
        return serviceSn;
    }

    public void setServiceSn(String serviceSn) {
        this.serviceSn = serviceSn;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getOpId() {
        return opId;
    }

    public void setOpId(String opId) {
        this.opId = opId;
    }

    public String getRequstTime() {
        return requstTime;
    }

    public void setRequstTime(String requstTime) {
        this.requstTime = requstTime;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getResServiceSn() {
        return resServiceSn;
    }

    public void setResServiceSn(String resServiceSn) {
        this.resServiceSn = resServiceSn;
    }

    public String getResServiceTime() {
        return resServiceTime;
    }

    public void setResServiceTime(String resServiceTime) {
        this.resServiceTime = resServiceTime;
    }

    public ServResponse getServResponse() {
        return servResponse;
    }

    public void setServResponse(ServResponse servResponse) {
        this.servResponse = servResponse;
    }
}
