package com.allinfinance.dev.core.util.hsp;

import com.allinfinance.dev.core.dto.hsp.HspBaseResponseDTO;
import com.allinfinance.dev.core.dto.hsp.SignatureGetBySM2PrivateKeyRequestDTO;
import com.allinfinance.dev.core.dto.hsp.SignatureGetBySM2PrivateKeyResponseDTO;
import com.allinfinance.dev.core.dto.hsp.SignatureVerifyBySM2PublicKeyRequestDTO;

/**
 * @author huanghf
 * @date 2022/6/21 14:24
 */
public interface SignatureService {
    /**
     * 用SM2私钥做签名--D306
     *
     * @param requestDTO 签名参数
     * @return 签名信息
     */
    HspBaseResponseDTO<SignatureGetBySM2PrivateKeyResponseDTO> getSignatureBySM2PrivateKey(SignatureGetBySM2PrivateKeyRequestDTO requestDTO);

    /**
     * 用SM2公钥做验签--D307
     *
     * @param requestDTO 验签参数
     * @return 验签结果
     */
    HspBaseResponseDTO verifySignatureBySM2PublicKey(SignatureVerifyBySM2PublicKeyRequestDTO requestDTO);
}
