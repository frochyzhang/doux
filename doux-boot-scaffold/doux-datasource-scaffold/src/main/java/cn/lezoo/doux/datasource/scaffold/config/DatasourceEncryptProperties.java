package cn.lezoo.doux.datasource.scaffold.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author qipeng
 * @date 2022/1/21 10:48
 */
@ConfigurationProperties(prefix = "cn.lezoo.datasource.encrypt")
@Configuration
public class DatasourceEncryptProperties {
    /**
     * 是否加密
     */
    private boolean encrypted;
    /**
     * 加密公钥
     */
    private String publicKey;

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

}
