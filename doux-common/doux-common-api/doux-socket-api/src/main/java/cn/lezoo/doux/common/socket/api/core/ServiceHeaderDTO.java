package cn.lezoo.doux.common.socket.api.core;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * CommonHeaderDto
 *
 * @author hongmr
 * @date 2017/6/19.
 */
public class ServiceHeaderDTO {
    @JacksonXmlProperty(localName = "SERVICE_SN")
    protected String serviceSn;
    @JacksonXmlProperty(localName = "SERVICE_ID")
    protected String serviceId;
    @JacksonXmlProperty(localName = "ORG")
    protected String org;
    @JacksonXmlProperty(localName = "CHANNEL_ID")
    protected String channelId;
    @JacksonXmlProperty(localName = "OP_ID")
    protected String opId;
    @JacksonXmlProperty(localName = "REQUST_TIME")
    protected String requestTime;
    @JacksonXmlProperty(localName = "VERSION_ID")
    protected String versionId;
    @JacksonXmlProperty(localName = "MAC")
    protected String mac;
    @JacksonXmlProperty(localName = "INNER_SYSTEM_IND")
    protected String innerSystemInd;
    @JacksonXmlProperty(localName = "RES_SERVICE_SN")
    protected String resServiceSn;
    @JacksonXmlProperty(localName = "RES_SERVICE_TIME")
    protected String resServiceTime;
    @JacksonXmlProperty(localName = "SERV_RESPONSE")
    protected ServResponse servResponse;

    public String getServiceSn() {
        return serviceSn;
    }

    public ServiceHeaderDTO setServiceSn(String serviceSn) {
        this.serviceSn = serviceSn;
        return this;
    }

    public String getServiceId() {
        return serviceId;
    }

    public ServiceHeaderDTO setServiceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public String getOrg() {
        return org;
    }

    public ServiceHeaderDTO setOrg(String org) {
        this.org = org;
        return this;
    }

    public String getChannelId() {
        return channelId;
    }

    public ServiceHeaderDTO setChannelId(String channelId) {
        this.channelId = channelId;
        return this;
    }

    public String getOpId() {
        return opId;
    }

    public ServiceHeaderDTO setOpId(String opId) {
        this.opId = opId;
        return this;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public ServiceHeaderDTO setRequestTime(String requestTime) {
        this.requestTime = requestTime;
        return this;
    }

    public String getVersionId() {
        return versionId;
    }

    public ServiceHeaderDTO setVersionId(String versionId) {
        this.versionId = versionId;
        return this;
    }

    public String getMac() {
        return mac;
    }

    public ServiceHeaderDTO setMac(String mac) {
        this.mac = mac;
        return this;
    }

    public String getInnerSystemInd() {
        return innerSystemInd;
    }

    public ServiceHeaderDTO setInnerSystemInd(String innerSystemInd) {
        this.innerSystemInd = innerSystemInd;
        return this;
    }

    public String getResServiceSn() {
        return resServiceSn;
    }

    public ServiceHeaderDTO setResServiceSn(String resServiceSn) {
        this.resServiceSn = resServiceSn;
        return this;
    }

    public String getResServiceTime() {
        return resServiceTime;
    }

    public ServiceHeaderDTO setResServiceTime(String resServiceTime) {
        this.resServiceTime = resServiceTime;
        return this;
    }

    public ServResponse getServResponse() {
        return servResponse;
    }

    public ServiceHeaderDTO setServResponse(ServResponse servResponse) {
        this.servResponse = servResponse;
        return this;
    }

    @Override
    public String toString() {
        return "ServiceHeaderDTO{" +
                "serviceSn='" + serviceSn + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", org='" + org + '\'' +
                ", channelId='" + channelId + '\'' +
                ", opId='" + opId + '\'' +
                ", requestTime='" + requestTime + '\'' +
                ", versionId='" + versionId + '\'' +
                ", mac='" + mac + '\'' +
                ", innerSystemInd='" + innerSystemInd + '\'' +
                ", resServiceSn='" + resServiceSn + '\'' +
                ", resServiceTime='" + resServiceTime + '\'' +
                ", servResponse=" + servResponse +
                '}';
    }
}
