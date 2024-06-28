package cn.lezoo.doux.common.socket.api.core;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * CommonHeaderDto
 *
 * @author hongmr
 * @date 2017/6/19.
 */
@Data
@Accessors(chain = true)
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
}
