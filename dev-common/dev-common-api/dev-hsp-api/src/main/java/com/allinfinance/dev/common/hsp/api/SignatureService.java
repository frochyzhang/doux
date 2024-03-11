package com.allinfinance.dev.common.hsp.api;


import com.allinfinance.dev.common.hsp.api.dto.HspBaseResponseDTO;
import com.allinfinance.dev.common.hsp.api.dto.SignatureGetBySM2PrivateKeyRequestDTO;
import com.allinfinance.dev.common.hsp.api.dto.SignatureGetBySM2PrivateKeyResponseDTO;
import com.allinfinance.dev.common.hsp.api.dto.SignatureVerifyBySM2PublicKeyRequestDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author huanghf
 * @date 2022/6/21 14:24
 */
public interface SignatureService {
    String PREFIX = "/hsp/api/signature";

    /**
     * 用SM2私钥做签名--D306
     *
     * @param requestDTO 签名参数
     * @return 签名信息
     */
    @PostMapping("/sign")
    HspBaseResponseDTO<SignatureGetBySM2PrivateKeyResponseDTO> getSignatureBySM2PrivateKey(@RequestBody SignatureGetBySM2PrivateKeyRequestDTO requestDTO);

    /**
     * 用SM2公钥做验签--D307
     *
     * @param requestDTO 验签参数
     * @return 验签结果
     */
    @PostMapping("/verify")
    HspBaseResponseDTO verifySignatureBySM2PublicKey(@RequestBody SignatureVerifyBySM2PublicKeyRequestDTO requestDTO);
}
