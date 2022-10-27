package com.allinfinance.dev.tools.hsp.listener;

import com.alibaba.nacos.spring.context.event.config.NacosConfigReceivedEvent;
import com.allinfinance.dev.core.dto.hsp.HspBaseResponseDTO;
import com.allinfinance.dev.core.dto.hsp.SignatureGetBySM2PrivateKeyRequestDTO;
import com.allinfinance.dev.core.dto.hsp.SignatureGetBySM2PrivateKeyResponseDTO;
import com.allinfinance.dev.core.dto.hsp.SignatureVerifyBySM2PublicKeyRequestDTO;
import com.allinfinance.dev.core.util.hsp.SignatureService;
import com.allinfinance.dev.tools.hsp.DTOMapper;
import com.allinfinance.dev.tools.hsp.config.HspConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author qipeng
 * @date 2022/10/27 11:08
 * @desc
 */
@Component
public class NacosConfigListener implements ApplicationListener<NacosConfigReceivedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(NacosConfigListener.class);

    @Autowired
    private HspConfig hspConfig;

    @Autowired
    private SignatureService signatureService;

    @Override
    public void onApplicationEvent(NacosConfigReceivedEvent event) {
        logger.info("接收到配置更新, dataId={}, content={}", event.getDataId(), event.getContent());

        if (hspConfig.getSignConfig().getStart()) {
            logger.info("签名开始...");
            SignatureGetBySM2PrivateKeyRequestDTO requestDTO = DTOMapper.INSTANCE.convertSignDTO(hspConfig.getSignConfig());
            SignatureGetBySM2PrivateKeyResponseDTO signResponse = signatureService.getSignatureBySM2PrivateKey(requestDTO).getResponse();
            String signatureR = signResponse.getSignatureR();
            String signatureS = signResponse.getSignatureS();
            logger.info("签名结束：signatureR={}，signatureS={}", signatureR, signatureS);
            logger.info("=======================================================");
        }

        if (hspConfig.getVerifyConfig().getStart()) {
            logger.info("验签开始...");
            SignatureVerifyBySM2PublicKeyRequestDTO requestDTO = DTOMapper.INSTANCE.convertVerifyDTO(hspConfig.getVerifyConfig());
            HspBaseResponseDTO hspBaseResponseDTO = signatureService.verifySignatureBySM2PublicKey(requestDTO);
            Boolean success = hspBaseResponseDTO.getSuccess();
            if (success) {
                logger.info("验签通过");
            } else {
                logger.error("验签失败：{}", hspBaseResponseDTO);
            }
            logger.info("=============================");
        }
    }
}
