package cn.lezoo.doux.common.hsp.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author huanghf
 * @date 2022/6/19 13:54
 */
@Data
@Accessors(chain = true)
public class SignatureGetBySM2PrivateKeyRequestDTO implements Serializable {
    /**
     * 外部输入密钥，HEX
     */
    @Size(min = 1, max = 65535 * 2)
    private String privateKey;
    /**
     * 证书序列号，明文
     */
    @Size(min = 1, max = 65535 / 2)
    private String certId;
    /**
     * 数据，明文
     */
    @Size(min = 1, max = 65535 / 2)
    private String data;
}
