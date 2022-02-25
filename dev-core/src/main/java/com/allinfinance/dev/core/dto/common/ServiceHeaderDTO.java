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
    protected String requestTime;
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
                ", requestTime='" + requestTime + '\'' +
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

    public ServiceHeaderDTO() {
    }

    public ServiceHeaderDTO(String serviceSn, String serviceId, String org, String channelId, String opId, String requestTime, String versionId, String mac, String resServiceSn, String resServiceTime, ServResponse servResponse) {
        this.serviceSn = serviceSn;
        this.serviceId = serviceId;
        this.org = org;
        this.channelId = channelId;
        this.opId = opId;
        this.requestTime = requestTime;
        this.versionId = versionId;
        this.mac = mac;
        this.resServiceSn = resServiceSn;
        this.resServiceTime = resServiceTime;
        this.servResponse = servResponse;
    }

    public static ServiceHeaderDTO.ServiceHeaderDTOBuilder builder() {
        return new ServiceHeaderDTO.ServiceHeaderDTOBuilder();
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
        private String resServiceSn;
        private String resServiceTime;
        private ServResponse servResponse;

        ServiceHeaderDTOBuilder() {
        }

        public ServiceHeaderDTO.ServiceHeaderDTOBuilder serviceSn(String serviceSn) {
            this.serviceSn = serviceSn;
            return this;
        }

        public ServiceHeaderDTO.ServiceHeaderDTOBuilder serviceId(String serviceId) {
            this.serviceId = serviceId;
            return this;
        }

        public ServiceHeaderDTO.ServiceHeaderDTOBuilder org(String org) {
            this.org = org;
            return this;
        }

        public ServiceHeaderDTO.ServiceHeaderDTOBuilder channelId(String channelId) {
            this.channelId = channelId;
            return this;
        }

        public ServiceHeaderDTO.ServiceHeaderDTOBuilder opId(String opId) {
            this.opId = opId;
            return this;
        }

        public ServiceHeaderDTO.ServiceHeaderDTOBuilder requestTime(String requestTime) {
            this.requestTime = requestTime;
            return this;
        }

        public ServiceHeaderDTO.ServiceHeaderDTOBuilder versionId(String versionId) {
            this.versionId = versionId;
            return this;
        }

        public ServiceHeaderDTO.ServiceHeaderDTOBuilder mac(String mac) {
            this.mac = mac;
            return this;
        }

        public ServiceHeaderDTO.ServiceHeaderDTOBuilder resServiceSn(String resServiceSn) {
            this.resServiceSn = resServiceSn;
            return this;
        }

        public ServiceHeaderDTO.ServiceHeaderDTOBuilder resServiceTime(String resServiceTime) {
            this.resServiceTime = resServiceTime;
            return this;
        }

        public ServiceHeaderDTO.ServiceHeaderDTOBuilder servResponse(ServResponse servResponse) {
            this.servResponse = servResponse;
            return this;
        }

        public ServiceHeaderDTO build() {
            return new ServiceHeaderDTO(this.serviceSn, this.serviceId, this.org, this.channelId, this.opId, this.requestTime, this.versionId, this.mac, this.resServiceSn, this.resServiceTime, this.servResponse);
        }

        public String toString() {
            return "ServiceHeaderDTO.ServiceHeaderDTOBuilder(serviceSn=" + this.serviceSn + ", serviceId=" + this.serviceId + ", org=" + this.org + ", channelId=" + this.channelId + ", opId=" + this.opId + ", requestTime=" + this.requestTime + ", versionId=" + this.versionId + ", mac=" + this.mac + ", resServiceSn=" + this.resServiceSn + ", resServiceTime=" + this.resServiceTime + ", servResponse=" + this.servResponse + ")";
        }
    }
}
