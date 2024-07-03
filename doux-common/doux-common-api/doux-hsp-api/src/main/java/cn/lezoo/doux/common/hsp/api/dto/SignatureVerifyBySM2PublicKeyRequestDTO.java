package cn.lezoo.doux.common.hsp.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author huanghf
 * @date 2022/6/21 16:33
 */
@Data
@Accessors(chain = true)
public class SignatureVerifyBySM2PublicKeyRequestDTO implements Serializable {
    /**
     * 公钥明文X, HEX
     */
    @Size(min = 32 * 2, max = 32 * 2)
    private String plainPublicKeyX;
    /**
     * 公钥明文Y, HEX
     */
    @Size(min = 32 * 2, max = 32 * 2)
    private String plainPublicKeyY;
    /**
     * 签名结果R, HEX
     */
    @Size(min = 32 * 2, max = 32 * 2)
    private String signatureR;
    /**
     * 签名结果S, HEX
     */
    @Size(min = 32 * 2, max = 32 * 2)
    private String signatureS;
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
