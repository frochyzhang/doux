package com.allinfinance.dev.ccs.content;

import com.allinfinance.dev.ccs.utils.RSAUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * RSA工具封装类
 * Created
 */
@Component("rsaKeyProperties")
@ConfigurationProperties(prefix = "rsa.key")
public class RSAKeyProperties {

    private String publicKeyFile;
    private String privateKeyFile;

    private PublicKey publicKey;
    private PrivateKey privateKey;
    private String secret;

    @PostConstruct
    public void createRSAKey() throws Exception {
        RSAUtils.generateKey(publicKeyFile, privateKeyFile, secret, 0);
        this.publicKey = RSAUtils.getPublicKey(publicKeyFile);
        this.privateKey = RSAUtils.getPrivateKey(privateKeyFile);
    }

    public String getPublicKeyFile() {
        return publicKeyFile;
    }

    public void setPublicKeyFile(String publicKeyFile) {
        this.publicKeyFile = publicKeyFile;
    }

    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    public void setPrivateKeyFile(String privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
