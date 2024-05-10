package cn.lezoo.doux.white.list.diversion.http.config;

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
    /**
     * 白名单开关
     */
    private Boolean onFlag = Boolean.FALSE;
    /**
     * 转发新服务地址-ip:port
     */
    private String url;
    /**
     * 报文编码格式
     */
    private String msgEncode = "UTF-8";
    /**
     * 转发新服务超时时间，单位：秒
     */
    private Integer timeoutSec = 3;
    /**
     * 白名单配置列表
     */
    private Map<String, WhiteListKeyParam> whiteListKeys = new HashMap<>();

    public Boolean getOnFlag() {
        return onFlag;
    }

    public WhiteListConfig setOnFlag(Boolean onFlag) {
        this.onFlag = onFlag;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public WhiteListConfig setUrl(String url) {
        this.url = url;
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

    public WhiteListKeyParam getWhiteListKey(String whiteListKey) {
        return whiteListKeys.get(whiteListKey);
    }

    @Override
    public String toString() {
        return "WhiteListConfig{" +
                "onFlag=" + onFlag +
                ", url='" + url + '\'' +
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
