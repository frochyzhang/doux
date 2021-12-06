package com.allinfinance.dev.batch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2021/12/6 10:46
 */
@ConfigurationProperties(prefix = XxlJobProperties.PREFIX)
public class XxlJobProperties {
    public static final String PREFIX = "dev.batch.xxl.job";
    /**
     * 管理页面地址
     */
    private String adminAddresses;
    /**
     * 应用名
     */
    private String appName;
    /**
     * 发布地址(可为空)
     */
    private String address = "";
    /**
     * 发布IP(可为空)
     */
    private String ip = "";
    /**
     * 发布端口(默认9999)
     */
    private Integer port;
    /**
     * 私有token(可为空)
     */
    private String accessToken = "";
    /**
     * 指定xxl日志路径,(默认路径:data/applogs/xxl-job/jobhandler)
     */
    private String logPath = "data/applogs/xxl-job/jobhandler";
    /**
     * xxl日志保留周期(默认为30天)
     */
    private Integer logRetentionDays = 30;

    public String getAdminAddresses() {
        return adminAddresses;
    }

    public void setAdminAddresses(String adminAddresses) {
        this.adminAddresses = adminAddresses;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public Integer getLogRetentionDays() {
        return logRetentionDays;
    }

    public void setLogRetentionDays(Integer logRetentionDays) {
        this.logRetentionDays = logRetentionDays;
    }
}
