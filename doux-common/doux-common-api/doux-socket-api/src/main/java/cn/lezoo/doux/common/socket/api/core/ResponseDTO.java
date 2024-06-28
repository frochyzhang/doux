package cn.lezoo.doux.common.socket.api.core;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author huanghf
 * @date 2024/1/8 14:56
 */
@Data
@Accessors(chain = true)
@JacksonXmlRootElement(localName = "SERVICE")
public class ResponseDTO<T> {
    @JacksonXmlProperty(isAttribute = true)
    protected String xmlns = "http://www.allinfinance.com/dataspec/";

    @JacksonXmlProperty(localName = "SERVICE_HEADER")
    protected ServiceHeaderDTO header;

    @JacksonXmlProperty(localName = "SERVICE_BODY")
    protected ResponseBodyDTO<T> responseBody;
}
