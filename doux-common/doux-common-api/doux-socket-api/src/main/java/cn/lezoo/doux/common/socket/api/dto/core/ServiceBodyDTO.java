package cn.lezoo.doux.common.socket.api.dto.core;

import cn.lezoo.doux.common.socket.api.core.RequestBodyDTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/24 14:52
 * @deprecated 请使用 {@link RequestBodyDTO}
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceBodyDTO {
    @XStreamAlias("EXT_ATTRIBUTES")
    protected ExtAttributes extAttributes;

    public ServiceBodyDTO() {
    }

    public ServiceBodyDTO(ExtAttributes extAttributes) {
        this.extAttributes = extAttributes;
    }

    @Override
    public String toString() {
        return "ServiceBodyDTO{" +
                "extAttributes=" + extAttributes +
                '}';
    }

    public ExtAttributes getExtAttributes() {
        return extAttributes;
    }

    public void setExtAttributes(ExtAttributes extAttributes) {
        this.extAttributes = extAttributes;
    }
}
