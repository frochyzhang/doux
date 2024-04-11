package com.allinfinance.dev.white.list.diversion.http.config;

import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author huanghf
 * @date 2024/3/28 21:24
 */
@ConfigurationProperties(prefix = "com.allinfinance.backup")
@NacosConfigurationProperties(groupId = Constants.DEFAULT_GROUP, prefix = "com.allinfinance.backup", dataId = "white-list-config",
        type = ConfigType.YAML, autoRefreshed = true)
public class TrafficBackupConfig {
    /**
     * 流量备份开关
     */
    private Boolean onFlag = Boolean.FALSE;
    /**
     * 是否开启异步记录日志
     */
    private Boolean enableAsync = Boolean.FALSE;
    /**
     * 日志等级
     */
    private String level = "info";
    /**
     * 日志保留时间
     */
    private Integer retainDays = 30;
    /**
     * 日志文件名称
     */
    private String file;
    /**
     * 日志文件路径
     */
    private String filePath;

    public Boolean getOnFlag() {
        return onFlag;
    }

    public TrafficBackupConfig setOnFlag(Boolean onFlag) {
        this.onFlag = onFlag;
        return this;
    }

    public Boolean getEnableAsync() {
        return enableAsync;
    }

    public TrafficBackupConfig setEnableAsync(Boolean enableAsync) {
        this.enableAsync = enableAsync;
        return this;
    }

    public String getLevel() {
        return level;
    }

    public TrafficBackupConfig setLevel(String level) {
        this.level = level;
        return this;
    }

    public Integer getRetainDays() {
        return retainDays;
    }

    public TrafficBackupConfig setRetainDays(Integer retainDays) {
        this.retainDays = retainDays;
        return this;
    }

    public String getFile() {
        return file;
    }

    public TrafficBackupConfig setFile(String file) {
        this.file = file;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public TrafficBackupConfig setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    @Override
    public String toString() {
        return "TrafficBackupConfig{" +
                "onFlag=" + onFlag +
                ", enableAsync=" + enableAsync +
                ", level='" + level + '\'' +
                ", retainDays=" + retainDays +
                ", file='" + file + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
