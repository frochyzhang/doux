package com.allinfinance.dev.hsp.service;

import cn.hutool.core.util.HexUtil;
import com.allinfinance.dev.common.hsp.api.SignatureService;
import com.allinfinance.dev.common.hsp.api.dto.*;
import com.allinfinance.dev.connection.pool.scaffold.api.MessagePorter;
import com.allinfinance.dev.hsp.constant.AlgorithmEnum;
import com.allinfinance.dev.hsp.constant.HashFlagEnum;
import com.allinfinance.dev.hsp.constant.InstructionEnum;
import com.allinfinance.dev.hsp.constant.RespCodeEnum;
import com.allinfinance.dev.hsp.util.DigestUtil;
import com.allinfinance.dev.hsp.util.ValidateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author huanghf
 * @date 2022/6/19 14:47
 */
@Service(value = "signatureService")
public class SignatureServiceImpl implements SignatureService {
    private static final Logger logger = LoggerFactory.getLogger(SignatureServiceImpl.class);

    @Autowired
    private DigestUtil digestUtil;

    @Autowired
    private MessagePorter messagePorter;

    /**
     * 用SM2私钥做签名--D306
     *
     * @param requestDTO 签名参数
     * @return 签名信息
     */
    @Override
    public HspBaseResponseDTO<SignatureGetBySM2PrivateKeyResponseDTO> getSignatureBySM2PrivateKey(SignatureGetBySM2PrivateKeyRequestDTO requestDTO) {
        logger.info("开始处理SM2私钥签名请求，校验请求参数，request: {}", requestDTO);
        try {
            ValidateUtil.validateValue(requestDTO);
        } catch (IllegalArgumentException e) {
            logger.error("请求参数不符合格式要求", e);
            return HspBaseResponseDTO.fail(e.getMessage());
        }
        logger.info("开始组装SM2私钥签名加密机指令");
        String digest = digestUtil.getDigest(HashAlgorithmEnum.SM3, requestDTO.getData());
        if (StringUtils.isBlank(digest)) {
            logger.error("请求加密机获取摘要失败");
            return HspBaseResponseDTO.fail("请求加密机获取摘要失败");
        }
        StringBuilder instruction = new StringBuilder();
        String certIdHex = HexUtil.encodeHexStr(requestDTO.getCertId());
        instruction.append(InstructionEnum.D306.getCode())
                .append("FFFF")
                //字节数
                .append(String.format("%04x", requestDTO.getPrivateKey().length() / 2))
                .append(requestDTO.getPrivateKey())
                .append(AlgorithmEnum.SM4.getCode())
                .append(HashFlagEnum.YES.getCode())
                .append(String.format("%04x", certIdHex.length() / 2))
                .append(certIdHex)
                .append(String.format("%04x", digest.length() / 2))
                .append(digest);

        logger.debug("请求加密机报文: {}", instruction);
        String response = messagePorter.writeAndFlush(instruction.toString());
        logger.debug("加密机返回报文: {}", response);

        if (StringUtils.isBlank(response)) {
            logger.error("请求加密机获取签名响应为空");
            return HspBaseResponseDTO.fail("请求加密机获取签名失败");
        }

        int offset = 0;
        int div = 2;
        String respCode = response.substring(offset, div);
        logger.info("加密机返回报文应答码: {}", respCode);
        offset += div;
        if (RespCodeEnum.SUCCESS.getRespCode().equals(respCode)) {
            div = 64;
            String signatureRHex = response.substring(offset, offset + div);
            offset += div;
            String signatureSHex = response.substring(offset, offset + div);
            logger.info("加密机返回成功，签名结果R部分: {}, 签名结果S部分: {}", signatureRHex, signatureSHex);
            SignatureGetBySM2PrivateKeyResponseDTO signatureGetBySM2PrivateKeyResponseDTO = new SignatureGetBySM2PrivateKeyResponseDTO();
            signatureGetBySM2PrivateKeyResponseDTO.setSignatureR(signatureRHex);
            signatureGetBySM2PrivateKeyResponseDTO.setSignatureS(signatureSHex);
            return HspBaseResponseDTO.success("获取签名成功", signatureGetBySM2PrivateKeyResponseDTO);
        } else {
            logger.error("加密机返回失败，错误码: {}", response.substring(offset, offset + div));
            return HspBaseResponseDTO.fail("请求加密机获取签名失败");
        }
    }

    /**
     * 用SM2公钥做验签--D307
     *
     * @param requestDTO 验签参数
     * @return 验签结果
     */
    @Override
    public HspBaseResponseDTO verifySignatureBySM2PublicKey(SignatureVerifyBySM2PublicKeyRequestDTO requestDTO) {
        logger.info("开始处理SM2公钥验签请求，校验请求参数，request: {}", requestDTO);
        try {
            ValidateUtil.validateValue(requestDTO);
        } catch (IllegalArgumentException e) {
            logger.error("请求参数不符合格式要求", e);
            return HspBaseResponseDTO.fail(e.getMessage());
        }
        logger.info("开始组装SM2公钥验签加密机指令");
        StringBuilder instruction = new StringBuilder();
        String digest = digestUtil.getDigest(HashAlgorithmEnum.SM3, requestDTO.getData());
        if (StringUtils.isBlank(digest)) {
            logger.error("请求加密机获取摘要失败");
            return HspBaseResponseDTO.fail("请求加密机获取摘要失败");
        }
        String certIdHex = HexUtil.encodeHexStr(requestDTO.getCertId());
        instruction.append(InstructionEnum.D307.getCode())
                .append("FFFF")
                .append(requestDTO.getPlainPublicKeyX())
                .append(requestDTO.getPlainPublicKeyY())
                .append(requestDTO.getSignatureR())
                .append(requestDTO.getSignatureS())
                .append(HashFlagEnum.YES.getCode())
                .append(String.format("%04x", certIdHex.length() / 2))
                .append(certIdHex)
                .append(String.format("%04x", digest.length() / 2))
                .append(digest);

        logger.debug("请求加密机报文: {}", instruction);
        String response = messagePorter.writeAndFlush(instruction.toString());
        logger.debug("加密机返回报文: {}", response);

        if (StringUtils.isBlank(response)) {
            logger.error("请求加密机验签响应为空");
            return HspBaseResponseDTO.fail("验签失败");
        }

        int offset = 0;
        int div = 2;
        String respCode = response.substring(offset, div);
        logger.info("加密机返回报文应答码: {}", respCode);
        offset += div;
        if (RespCodeEnum.SUCCESS.getRespCode().equals(respCode)) {
            logger.info("验签成功");
            return HspBaseResponseDTO.success("验签成功", null);
        } else {
            logger.error("加密机返回失败，错误码: {}", response.substring(offset, offset + div));
            return HspBaseResponseDTO.fail("验签失败");
        }
    }
}
