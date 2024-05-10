package cn.lezoo.doux.common.hsp.api.dto;


import cn.lezoo.doux.common.dictionary.validate.Check;

import java.io.Serializable;

/**
 * @author huanghf
 * @date 2022/6/19 13:54
 */
public class SignatureGetBySM2PrivateKeyRequestDTO implements Serializable {
    /**
     * 外部输入密钥，HEX
     */
    @Check(minLength = 1, maxLength = 65535 * 2)
    private String privateKey;
    /**
     * 证书序列号，明文
     */
    @Check(minLength = 1, maxLength = 65535 / 2)
    private String certId;
    /**
     * 数据，明文
     */
    @Check(minLength = 1, maxLength = 65535 / 2)
    private String data;

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
        return "SignatureGetBySM2PrivateKeyRequestDTO{" +
                "privateKey='" + privateKey + '\'' +
                ", certId='" + certId + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
