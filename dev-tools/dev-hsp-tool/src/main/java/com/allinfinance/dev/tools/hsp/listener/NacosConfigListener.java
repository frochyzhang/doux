package com.allinfinance.dev.tools.hsp.listener;

import cn.hutool.core.util.HexUtil;
import com.alibaba.boot.nacos.config.properties.NacosConfigProperties;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.spring.context.event.config.NacosConfigReceivedEvent;
import com.allinfinance.dev.core.dto.hsp.HspBaseResponseDTO;
import com.allinfinance.dev.core.dto.hsp.SignatureGetBySM2PrivateKeyRequestDTO;
import com.allinfinance.dev.core.dto.hsp.SignatureGetBySM2PrivateKeyResponseDTO;
import com.allinfinance.dev.core.dto.hsp.SignatureVerifyBySM2PublicKeyRequestDTO;
import com.allinfinance.dev.core.util.hsp.SignatureService;
import com.allinfinance.dev.tools.hsp.DTOMapper;
import com.allinfinance.dev.tools.hsp.config.HspConfig;
import com.allinfinance.dev.tools.hsp.util.Sm2CertUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.x509.Certificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Properties;

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
    private NacosConfigProperties nacosConfigProperties;

    @Autowired
    private SignatureService signatureService;

    @Override
    public void onApplicationEvent(NacosConfigReceivedEvent event) {
        logger.info("接收到配置更新, dataId={}, content={}", event.getDataId(), event.getContent());

        logger.info("-- 签名/验签操作 --");
        if (hspConfig.getSignConfig().getStart()) {
            logger.info("签名开始...");
            SignatureGetBySM2PrivateKeyRequestDTO requestDTO = new SignatureGetBySM2PrivateKeyRequestDTO();
            requestDTO.setPrivateKey(hspConfig.getSignConfig().getPrivateKey());
            requestDTO.setCertId(HexUtil.decodeHexStr(HexUtil.encodeHexStr(hspConfig.getSignConfig().getCertId().getBytes())));
            requestDTO.setData(hspConfig.getSignConfig().getData());
            SignatureGetBySM2PrivateKeyResponseDTO signResponse = signatureService.getSignatureBySM2PrivateKey(requestDTO).getResponse();
            String signatureR = signResponse.getSignatureR();
            String signatureS = signResponse.getSignatureS();
            logger.info("签名结束：signatureR={}，signatureS={}", signatureR, signatureS);

            logger.info("Base64签名结果: {}", Base64.encodeBase64String(HexUtil.decodeHex(signatureR + signatureS)));
            logger.info("=======================================================");
        }

        if (hspConfig.getVerifyConfig().getStart()) {
            logger.info("验签开始...");
            SignatureVerifyBySM2PublicKeyRequestDTO requestDTO = new SignatureVerifyBySM2PublicKeyRequestDTO();
            byte[] sign = Base64.decodeBase64(hspConfig.getVerifyConfig().getSignatureResult());
            String signHexStr = HexUtil.encodeHexStr(sign);
            String publicKey = parsePublicKey(fetchNacosConfig(hspConfig.getVerifyConfig().getCertId()));
            requestDTO.setPlainPublicKeyX(publicKey.substring(0, 64));
            requestDTO.setPlainPublicKeyY(publicKey.substring(64));
            requestDTO.setSignatureR(signHexStr.substring(0, 64));
            requestDTO.setSignatureS(signHexStr.substring(64));
            requestDTO.setCertId(HexUtil.decodeHexStr(HexUtil.encodeHexStr(hspConfig.getVerifyConfig().getCertId().getBytes())));
            requestDTO.setData(hspConfig.getVerifyConfig().getData());
            HspBaseResponseDTO hspBaseResponseDTO = signatureService.verifySignatureBySM2PublicKey(requestDTO);
            Boolean success = hspBaseResponseDTO.getSuccess();
            if (success) {
                logger.info("验签通过");
            } else {
                logger.error("验签失败：{}", hspBaseResponseDTO);
            }
            logger.info("=======================================================");
        }

    }

    private String parsePublicKey(String certContent) {
        Certificate cert = null;
        String hexStr = null;
        try {
            cert = Sm2CertUtils.certFrom(Base64.decodeBase64(certContent));
        } catch (Exception e) {
            logger.error("证书加载失败", e);
        }
        byte[] publicKey = Sm2CertUtils.getPublicKey(cert);
        hexStr = HexUtil.encodeHexStr(publicKey);
        logger.info("公钥证书：{}", hexStr);
        return hexStr;
    }

    private String fetchNacosConfig(String configName) {
        logger.info("读取配置请求开始...");
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, nacosConfigProperties.getServerAddr());
        properties.put(PropertyKeyConst.NAMESPACE, nacosConfigProperties.getNamespace());
        logger.info("nacos远程配置信息:{}", properties);
        String configContent = null;
        try {
            ConfigService configService = NacosFactory.createConfigService(properties);
            configContent = configService.getConfig(configName, Constants.DEFAULT_GROUP, 3000L);
            if (StringUtils.isNotBlank(configContent)) {
                configContent = configContent.replaceAll("-----BEGIN CERTIFICATE-----", "")
                        .replaceAll("-----END CERTIFICATE-----", "")
                        .replaceAll("\\r", "").replaceAll("\\n", "");
            }
        } catch (NacosException e) {
            logger.error("获取nacos配置失败");
        }
        return configContent;
    }
}
