package com.allinfinance.dev.hsp.service;

import cn.hutool.core.util.HexUtil;
import com.allinfinance.dev.core.dto.hsp.*;
import com.allinfinance.dev.core.dto.hsp.constant.HashAlgorithmEnum;
import com.allinfinance.dev.core.util.hsp.ISignatureService;
import com.allinfinance.dev.core.util.hsp.ISummaryService;
import com.allinfinance.dev.hsp.constant.AlgorithmEnum;
import com.allinfinance.dev.hsp.constant.HashFlagEnum;
import com.allinfinance.dev.hsp.constant.InstructionEnum;
import com.allinfinance.dev.hsp.constant.RespCodeEnum;
import com.allinfinance.dev.hsp.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author huanghf
 * @date 2022/6/19 14:47
 */
@Service
public class SignatureServiceImpl implements ISignatureService {
    private static final Logger logger = LoggerFactory.getLogger(SignatureServiceImpl.class);

    @Autowired
    private ISummaryService summaryService;

    /**
     * 用SM2私钥做签名--D306
     *
     * @param requestDTO 签名参数
     * @return 签名信息
     */
    @Override
    public SignatureGetBySM2PrivateKeyResponseDTO getSignatureBySM2PrivateKey(SignatureGetBySM2PrivateKeyRequestDTO requestDTO) {
        logger.info("开始组装SM2私钥签名加密机指令");
        SummaryGetRequestDTO summaryGetRequestDTO = new SummaryGetRequestDTO();
        summaryGetRequestDTO.setData(requestDTO.getData());
        summaryGetRequestDTO.setHashAlgorithm(HashAlgorithmEnum.SM3);
        SummaryGetResponseDTO summaryGetResponseDTO = summaryService.getSummary(summaryGetRequestDTO);
        StringBuilder instruction = new StringBuilder();
        String externalInputKeyHex = HexUtil.encodeHexStr(requestDTO.getExternalInputKey());
        String certIdHex = HexUtil.encodeHexStr(requestDTO.getCertId());
        String summaryHex = HexUtil.encodeHexStr(summaryGetResponseDTO.getSummaryData());
        instruction.append(InstructionEnum.D306.getCode())
                .append("FFFF")
                //字节数
                .append(StrUtil.getLengthStr(Integer.toHexString(externalInputKeyHex.length() / 2), 4))
                .append(externalInputKeyHex)
                .append(AlgorithmEnum.SM4.getCode())
                .append(HashFlagEnum.YES.getCode())
                .append(StrUtil.getLengthStr(Integer.toHexString(certIdHex.length() / 2), 4))
                .append(certIdHex)
                .append(StrUtil.getLengthStr(Integer.toHexString(summaryHex.length() / 2), 4))
                .append(summaryHex);

        logger.debug("请求加密机报文: {}", instruction);
        // TODO: 2022/6/21 获取连接请求加密机
        String response = "";
        logger.debug("加密机返回报文: {}", response);

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
            SignatureGetBySM2PrivateKeyResponseDTO signatureGetBySM2PrivateKeyResponseDTO = new SignatureGetBySM2PrivateKeyResponseDTO(Boolean.TRUE);
            signatureGetBySM2PrivateKeyResponseDTO.setSignatureR(HexUtil.decodeHexStr(signatureRHex));
            signatureGetBySM2PrivateKeyResponseDTO.setSignatureS(HexUtil.decodeHexStr(signatureSHex));
            return signatureGetBySM2PrivateKeyResponseDTO;
        } else {
            logger.error("加密机返回失败，错误码: {}", response.substring(offset, offset + div));
            return new SignatureGetBySM2PrivateKeyResponseDTO(Boolean.FALSE);
        }
    }

    /**
     * 用SM2公钥做验签--D307
     *
     * @param requestDTO 验签参数
     * @return 验签结果
     */
    @Override
    public boolean verifySignatureBySM2PublicKey(SignatureVerifyBySM2PublicKeyRequestDTO requestDTO) {
        logger.info("开始组装SM2公钥验签加密机指令");
        SummaryGetRequestDTO summaryGetRequestDTO = new SummaryGetRequestDTO();
        summaryGetRequestDTO.setData(requestDTO.getData());
        summaryGetRequestDTO.setHashAlgorithm(HashAlgorithmEnum.SM3);
        SummaryGetResponseDTO summaryGetResponseDTO = summaryService.getSummary(summaryGetRequestDTO);
        StringBuilder instruction = new StringBuilder();
        String certIdHex = HexUtil.encodeHexStr(requestDTO.getCertId());
        String summaryHex = HexUtil.encodeHexStr(summaryGetResponseDTO.getSummaryData());
        instruction.append(InstructionEnum.D307.getCode())
                .append("FFFF")
                .append(HexUtil.encodeHexStr(requestDTO.getPlainPublicKeyX()))
                .append(HexUtil.encodeHexStr(requestDTO.getPlainPublicKeyY()))
                .append(HexUtil.encodeHexStr(requestDTO.getSignatureR()))
                .append(HexUtil.encodeHexStr(requestDTO.getSignatureS()))
                .append(HashFlagEnum.YES.getCode())
                .append(StrUtil.getLengthStr(Integer.toHexString(certIdHex.length() / 2), 4))
                .append(certIdHex)
                .append(StrUtil.getLengthStr(Integer.toHexString(summaryHex.length() / 2), 4))
                .append(summaryHex);

        logger.debug("请求加密机报文: {}", instruction);
        // TODO: 2022/6/21 获取连接请求加密机
        String response = "";
        logger.debug("加密机返回报文: {}", response);

        int offset = 0;
        int div = 2;
        String respCode = response.substring(offset, div);
        logger.info("加密机返回报文应答码: {}", respCode);
        offset += div;
        if (RespCodeEnum.SUCCESS.getRespCode().equals(respCode)) {
            logger.info("验签成功");
            return true;
        } else {
            logger.error("加密机返回失败，错误码: {}", response.substring(offset, offset + div));
            return false;
        }
    }
}
