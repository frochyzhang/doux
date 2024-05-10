package cn.lezoo.doux.white.list.diversion.socket.config;

import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huanghf
 * @date 2024/3/18 20:28
 */
@ConfigurationProperties(prefix = "cn.lezoo.white-list")
@NacosConfigurationProperties(groupId = Constants.DEFAULT_GROUP, prefix = "cn.lezoo.white-list", dataId = "white-list-config",
        type = ConfigType.YAML, autoRefreshed = true)
public class WhiteListConfig {
    private Boolean onFlag = Boolean.FALSE;
    private String ip;
    private String port;
    private String msgLengthSize = "6";
    private String msgEncode = "UTF-8";
    private Integer timeoutSec = 3;
    private Map<String, WhiteListKeyParam> whiteListKeys = new HashMap<>();

    public Boolean getOnFlag() {
        return onFlag;
    }

    public WhiteListConfig setOnFlag(Boolean onFlag) {
        this.onFlag = onFlag;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public WhiteListConfig setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getPort() {
        return port;
    }

    public WhiteListConfig setPort(String port) {
        this.port = port;
        return this;
    }

    public String getMsgLengthSize() {
        return msgLengthSize;
    }

    public WhiteListConfig setMsgLengthSize(String msgLengthSize) {
        this.msgLengthSize = msgLengthSize;
        return this;
    }

    public String getMsgEncode() {
        return msgEncode;
    }

    public WhiteListConfig setMsgEncode(String msgEncode) {
        this.msgEncode = msgEncode;
        return this;
    }

    public Integer getTimeoutSec() {
        return timeoutSec;
    }

    public WhiteListConfig setTimeoutSec(Integer timeoutSec) {
        this.timeoutSec = timeoutSec;
        return this;
    }

    public Map<String, WhiteListKeyParam> getWhiteListKeys() {
        return whiteListKeys;
    }

    public WhiteListConfig setWhiteListKeys(Map<String, WhiteListKeyParam> whiteListKeys) {
        this.whiteListKeys = whiteListKeys;
        return this;
    }

    @Override
    public String toString() {
        return "WhiteListConfig{" +
                "onFlag=" + onFlag +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", msgLengthSize='" + msgLengthSize + '\'' +
                ", msgEncode='" + msgEncode + '\'' +
                ", timeoutSec=" + timeoutSec +
                ", whiteListKeys=" + whiteListKeys +
                '}';
    }

    public static class WhiteListKeyParam {
        private String fieldName;
        private String desc;
        private Boolean cipher = Boolean.FALSE;
        private Boolean needMapping = Boolean.FALSE;

        public String getFieldName() {
            return fieldName;
        }

        public WhiteListKeyParam setFieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        public String getDesc() {
            return desc;
        }

        public WhiteListKeyParam setDesc(String desc) {
            this.desc = desc;
            return this;
        }

        public Boolean getCipher() {
            return cipher;
        }

        public WhiteListKeyParam setCipher(Boolean cipher) {
            this.cipher = cipher;
            return this;
        }

        public Boolean getNeedMapping() {
            return needMapping;
        }

        public WhiteListKeyParam setNeedMapping(Boolean needMapping) {
            this.needMapping = needMapping;
            return this;
        }

        @Override
        public String toString() {
            return "WhiteListKeyParam{" +
                    "fieldName='" + fieldName + '\'' +
                    ", desc='" + desc + '\'' +
                    ", cipher=" + cipher +
                    ", needMapping=" + needMapping +
                    '}';
        }
    }
}
