package cn.lezoo.doux.common.socket.api.core;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/24 14:52
 */
@Data
@Accessors(chain = true)
public class RequestBodyDTO<T> {
    @JacksonXmlProperty(localName = "EXT_ATTRIBUTES")
    protected ExtAttributes extAttributes;
    @JacksonXmlProperty(localName = "REQUEST")
    protected T request;
}
