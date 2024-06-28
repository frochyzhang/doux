package cn.lezoo.doux.common.socket.api.core;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * ServResponse
 *
 * @author hongmr
 * @date 2017/6/19
 */
@Data
@Accessors(chain = true)
public class ServResponse {
    /**
     * STATUS: S:交易成功 F:交易失败
     */
    @JacksonXmlProperty(localName = "STATUS")
    private String status;
    @JacksonXmlProperty(localName = "CODE")
    private String code;
    @JacksonXmlProperty(localName = "DESC")
    private String desc;
}
