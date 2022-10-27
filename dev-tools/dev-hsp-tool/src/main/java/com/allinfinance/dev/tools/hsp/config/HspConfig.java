package com.allinfinance.dev.tools.hsp.config;

import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author qipeng
 * @date 2022/10/27 10:09
 * @desc
 */
@Component
@NacosConfigurationProperties(groupId = Constants.DEFAULT_GROUP, type = ConfigType.YAML,
        dataId = "dev-hsp-tool", prefix = "dev.hsp.tool", autoRefreshed = true)
public class HspConfig {
    /**
     * 签名信息配置
     */
    private SignConfig signConfig;
    /**
     * 验签信息配置
     */
    private VerifyConfig verifyConfig;

    public static class SignConfig {
        /**
         * 该字段设置为true开始签名验签
         */
        private Boolean start;
        /**
         * 外部输入密钥，HEX
         */
        private String privateKey;
        /**
         * 证书序列号，明文
         */
        private String certId;
        /**
         * 数据，明文
         */
        private String data;

        public Boolean getStart() {
            return start;
        }

        public void setStart(Boolean start) {
            this.start = start;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }

        public String getCertId() {
            return certId;
        }

        public void setCertId(String certId) {
            this.certId = certId;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "SignConfig{" +
                    "start=" + start +
                    ", privateKey='" + privateKey + '\'' +
                    ", certId='" + certId + '\'' +
                    ", data='" + data + '\'' +
                    '}';
        }
    }

    public static class VerifyConfig {
        /**
         * 该字段设置为true开始签名验签
         */
        private Boolean start;
        /**
         * 公钥明文X, HEX
         */
        private String plainPublicKeyX;
        /**
         * 公钥明文Y, HEX
         */
        private String plainPublicKeyY;
        /**
         * 签名结果R, HEX
         */
        private String signatureR;
        /**
         * 签名结果S, HEX
         */
        private String signatureS;
        /**
         * 证书序列号，明文
         */
        private String certId;
        /**
         * 数据，明文
         */
        private String data;

        public Boolean getStart() {
            return start;
        }

        public void setStart(Boolean start) {
            this.start = start;
        }

        public String getPlainPublicKeyX() {
            return plainPublicKeyX;
        }

        public void setPlainPublicKeyX(String plainPublicKeyX) {
            this.plainPublicKeyX = plainPublicKeyX;
        }

        public String getPlainPublicKeyY() {
            return plainPublicKeyY;
        }

        public void setPlainPublicKeyY(String plainPublicKeyY) {
            this.plainPublicKeyY = plainPublicKeyY;
        }

        public String getSignatureR() {
            return signatureR;
        }

        public void setSignatureR(String signatureR) {
            this.signatureR = signatureR;
        }

        public String getSignatureS() {
            return signatureS;
        }

        public void setSignatureS(String signatureS) {
            this.signatureS = signatureS;
        }

        public String getCertId() {
            return certId;
        }

        public void setCertId(String certId) {
            this.certId = certId;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "VerifyConfig{" +
                    "start=" + start +
                    ", plainPublicKeyX='" + plainPublicKeyX + '\'' +
                    ", plainPublicKeyY='" + plainPublicKeyY + '\'' +
                    ", signatureR='" + signatureR + '\'' +
                    ", signatureS='" + signatureS + '\'' +
                    ", certId='" + certId + '\'' +
                    ", data='" + data + '\'' +
                    '}';
        }

    }

    public SignConfig getSignConfig() {
        return signConfig;
    }

    public void setSignConfig(SignConfig signConfig) {
        this.signConfig = signConfig;
    }

    public VerifyConfig getVerifyConfig() {
        return verifyConfig;
    }

    public void setVerifyConfig(VerifyConfig verifyConfig) {
        this.verifyConfig = verifyConfig;
    }

    @Override
    public String toString() {
        return "HspConfig{" +
                "signConfig=" + signConfig +
                ", verifyConfig=" + verifyConfig +
                '}';
    }
}
