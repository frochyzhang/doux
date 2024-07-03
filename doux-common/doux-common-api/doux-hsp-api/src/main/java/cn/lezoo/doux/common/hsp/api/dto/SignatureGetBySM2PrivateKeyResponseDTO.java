package cn.lezoo.doux.common.hsp.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author huanghf
 * @date 2022/6/19 14:46
 */
@Data
@Accessors(chain = true)
public class SignatureGetBySM2PrivateKeyResponseDTO implements Serializable {
    /**
     * 签名结果的R部分，HEX
     */
    @Size(min = 32 * 2, max = 32 * 2)
    private String signatureR;
    /**
     * 签名结果的S部分，HEX
     */
    @Size(min = 32 * 2, max = 32 * 2)
    private String signatureS;
}
