package cn.lezoo.doux.common.socket.api.core;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * ServResponse
 *
 * @author hongmr
 * @date 2017/6/19
 */
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

    public String getStatus() {
        return status;
    }

    public ServResponse setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ServResponse setCode(String code) {
        this.code = code;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public ServResponse setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    @Override
    public String toString() {
        return "ServResponse{" +
                "status='" + status + '\'' +
                ", code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
