package cn.lezoo.doux.tools.hsp.config;

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
         * 签名结果, base64
         */
        private String signatureResult;
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

        public String getSignatureResult() {
            return signatureResult;
        }

        public void setSignatureResult(String signatureResult) {
            this.signatureResult = signatureResult;
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
                    ", signatureResult='" + signatureResult + '\'' +
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
