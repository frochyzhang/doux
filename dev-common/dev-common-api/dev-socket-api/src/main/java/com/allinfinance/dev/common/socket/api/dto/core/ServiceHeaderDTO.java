package com.allinfinance.dev.common.socket.api.dto.core;

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
    protected String requestTime;
    @XStreamAlias("VERSION_ID")
    protected String versionId;
    @XStreamAlias("MAC")
    protected String mac;
    @XStreamAlias("INNER_SYSTEM_IND")
    protected String innerSystemInd;
    @XStreamAlias("RES_SERVICE_SN")
    protected String resServiceSn;
    @XStreamAlias("RES_SERVICE_TIME")
    protected String resServiceTime;
    @XStreamAlias("SERV_RESPONSE")
    protected ServResponse servResponse;

    public ServiceHeaderDTO() {
    }

    public ServiceHeaderDTO(String serviceSn, String serviceId, String org, String channelId, String opId, String requestTime, String versionId, String mac, String innerSystemInd, String resServiceSn, String resServiceTime, ServResponse servResponse) {
        this.serviceSn = serviceSn;
        this.serviceId = serviceId;
        this.org = org;
        this.channelId = channelId;
        this.opId = opId;
        this.requestTime = requestTime;
        this.versionId = versionId;
        this.mac = mac;
        this.innerSystemInd = innerSystemInd;
        this.resServiceSn = resServiceSn;
        this.resServiceTime = resServiceTime;
        this.servResponse = servResponse;
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

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
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

    public String getInnerSystemInd() {
        return innerSystemInd;
    }

    public void setInnerSystemInd(String innerSystemInd) {
        this.innerSystemInd = innerSystemInd;
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

    public static ServiceHeaderDTOBuilder builder() {
        return new ServiceHeaderDTOBuilder();
    }

    public static class ServiceHeaderDTOBuilder {
        private String serviceSn;
        private String serviceId;
        private String org;
        private String channelId;
        private String opId;
        private String requestTime;
        private String versionId;
        private String mac;
        protected String innerSystemInd;
        private String resServiceSn;
        private String resServiceTime;
        private ServResponse servResponse;

        ServiceHeaderDTOBuilder() {
        }

        public ServiceHeaderDTOBuilder serviceSn(String serviceSn) {
            this.serviceSn = serviceSn;
            return this;
        }

        public ServiceHeaderDTOBuilder serviceId(String serviceId) {
            this.serviceId = serviceId;
            return this;
        }

        public ServiceHeaderDTOBuilder org(String org) {
            this.org = org;
            return this;
        }

        public ServiceHeaderDTOBuilder channelId(String channelId) {
            this.channelId = channelId;
            return this;
        }

        public ServiceHeaderDTOBuilder opId(String opId) {
            this.opId = opId;
            return this;
        }

        public ServiceHeaderDTOBuilder requestTime(String requestTime) {
            this.requestTime = requestTime;
            return this;
        }

        public ServiceHeaderDTOBuilder versionId(String versionId) {
            this.versionId = versionId;
            return this;
        }

        public ServiceHeaderDTOBuilder mac(String mac) {
            this.mac = mac;
            return this;
        }

        public ServiceHeaderDTOBuilder resServiceSn(String resServiceSn) {
            this.resServiceSn = resServiceSn;
            return this;
        }

        public ServiceHeaderDTOBuilder innerSystemInd(String innerSystemInd) {
            this.innerSystemInd = innerSystemInd;
            return this;
        }


        public ServiceHeaderDTOBuilder resServiceTime(String resServiceTime) {
            this.resServiceTime = resServiceTime;
            return this;
        }

        public ServiceHeaderDTOBuilder servResponse(ServResponse servResponse) {
            this.servResponse = servResponse;
            return this;
        }

        public ServiceHeaderDTO build() {
            return new ServiceHeaderDTO(this.serviceSn, this.serviceId, this.org, this.channelId, this.opId, this.requestTime, this.versionId, this.mac, this.innerSystemInd, this.resServiceSn, this.resServiceTime, this.servResponse);
        }

        @Override
        public String toString() {
            return "ServiceHeaderDTOBuilder{" +
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
}
