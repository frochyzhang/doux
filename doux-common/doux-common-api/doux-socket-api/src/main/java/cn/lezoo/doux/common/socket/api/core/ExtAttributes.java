package cn.lezoo.doux.common.socket.api.core;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * ExtAttributes
 *
 * @author hongmr
 * @date 2017/6/19.
 */
@Data
@Accessors(chain = true)
public class ExtAttributes {
    @JacksonXmlProperty(localName = "AUTH")
    protected Auth auth;
}
