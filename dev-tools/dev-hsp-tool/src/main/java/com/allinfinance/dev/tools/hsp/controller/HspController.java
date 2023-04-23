package com.allinfinance.dev.tools.hsp.controller;

import cn.hutool.core.util.HexUtil;
import com.alibaba.boot.nacos.config.properties.NacosConfigProperties;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.allinfinance.dev.common.hsp.api.SignatureService;
import com.allinfinance.dev.common.hsp.api.dto.HspBaseResponseDTO;
import com.allinfinance.dev.common.hsp.api.dto.SignatureGetBySM2PrivateKeyRequestDTO;
import com.allinfinance.dev.common.hsp.api.dto.SignatureGetBySM2PrivateKeyResponseDTO;
import com.allinfinance.dev.common.hsp.api.dto.SignatureVerifyBySM2PublicKeyRequestDTO;
import com.allinfinance.dev.tools.hsp.config.HspConfig;
import com.allinfinance.dev.tools.hsp.util.Sm2CertUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.x509.Certificate;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

/**
 * @author huanghf
 * @date 2023/1/12 10:12
 */
@RestController
@RequestMapping("/hsp")
public class HspController implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(HspController.class);

    @Autowired
    private HspConfig hspConfig;

    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    @Autowired
    private SignatureService signatureService;

    private static String publicKey;

    @GetMapping
    public String hspTest() {
        try {
            if (hspConfig.getSignConfig().getStart()) {
                logger.info("签名开始...");
                SignatureGetBySM2PrivateKeyRequestDTO requestDTO = new SignatureGetBySM2PrivateKeyRequestDTO();
                requestDTO.setPrivateKey(hspConfig.getSignConfig().getPrivateKey());
                requestDTO.setCertId(HexUtil.decodeHexStr(HexUtil.encodeHexStr(hspConfig.getSignConfig().getCertId().getBytes())));
                requestDTO.setData(hspConfig.getSignConfig().getData());
                HspBaseResponseDTO<SignatureGetBySM2PrivateKeyResponseDTO> responseDTO = Assertions.assertDoesNotThrow(
                        () -> signatureService.getSignatureBySM2PrivateKey(requestDTO), "签名失败，调用HSP签名服务异常");
                Assertions.assertTrue(responseDTO.getSuccess(), responseDTO.getDesc());
                SignatureGetBySM2PrivateKeyResponseDTO signResponse = responseDTO.getResponse();
                String signatureR = signResponse.getSignatureR();
                String signatureS = signResponse.getSignatureS();
                logger.info("签名成功：signatureR={}，signatureS={}", signatureR, signatureS);
                logger.info("Base64签名结果: {}", Base64.encodeBase64String(HexUtil.decodeHex(signatureR + signatureS)));
            }
        } finally {
            logger.info("=======================================================");
        }

        try {
            if (hspConfig.getVerifyConfig().getStart()) {
                logger.info("验签开始...");
                SignatureVerifyBySM2PublicKeyRequestDTO requestDTO = new SignatureVerifyBySM2PublicKeyRequestDTO();
                byte[] sign = Base64.decodeBase64(hspConfig.getVerifyConfig().getSignatureResult());
                String signHexStr = HexUtil.encodeHexStr(sign);
                requestDTO.setPlainPublicKeyX(publicKey.substring(0, 64));
                requestDTO.setPlainPublicKeyY(publicKey.substring(64));
                requestDTO.setSignatureR(signHexStr.substring(0, 64));
                requestDTO.setSignatureS(signHexStr.substring(64));
                requestDTO.setCertId(HexUtil.decodeHexStr(HexUtil.encodeHexStr(hspConfig.getVerifyConfig().getCertId().getBytes())));
                requestDTO.setData(hspConfig.getVerifyConfig().getData());
                HspBaseResponseDTO responseDTO = Assertions.assertDoesNotThrow(
                        () -> signatureService.verifySignatureBySM2PublicKey(requestDTO), "验签失败，调用HSP验签服务异常");
                Assertions.assertTrue(responseDTO.getSuccess(), responseDTO.getDesc());
                logger.info("验签通过");
            }
        } finally {
            logger.info("=======================================================");
        }
        return "success";
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

    @Override
    public void afterPropertiesSet() throws Exception {
        publicKey = parsePublicKey(fetchNacosConfig(hspConfig.getVerifyConfig().getCertId()));
    }
}
