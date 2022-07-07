package com.allinfinance.dev.connection.pool.scaffold.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/7
 **/
@Configuration
public class ServerMetadataConfigure {
    /**
     * 服务端ip
     */
    public String serverIp;
    /**
     * 服务端端口
     */
    public String serverPort;
    /**
     * 最大活跃连接数
     */
    public String maxActiveConnections;
    /**
     * 最大空闲连接数
     */
    public String maxIdleConnections;
    /**
     * 最大空闲连接检查时间，超过该时间后要检查连接活跃度，单位：毫秒
     */
    public String maxCheckoutTime;
    /**
     * 最大的请求超时时间，超过该时间表示请求超时，单位：毫秒
     */
    public String defaultNetworkTimeout;
    /**
     * 连接重试等待时间，单位：毫秒
     */
    public String retryTimeToWait;
    /**
     * 本地能容忍的最大坏连接数
     */
    public String maxLocalBadConnectionTolerance;
    /**
     * ping请求内容
     */
    public String pingQueryContent;
    /**
     * ping验证内容
     */
    public String pingVerifyContent;
    /**
     * ping开关
     */
    public String pingEnabled;
    /**
     * 最近一次使用该连接的时间差，单位：毫秒
     */
    public String pingConnectionsNotUsed;

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getMaxActiveConnections() {
        return maxActiveConnections;
    }

    public void setMaxActiveConnections(String maxActiveConnections) {
        this.maxActiveConnections = maxActiveConnections;
    }

    public String getMaxIdleConnections() {
        return maxIdleConnections;
    }

    public void setMaxIdleConnections(String maxIdleConnections) {
        this.maxIdleConnections = maxIdleConnections;
    }

    public String getMaxCheckoutTime() {
        return maxCheckoutTime;
    }

    public void setMaxCheckoutTime(String maxCheckoutTime) {
        this.maxCheckoutTime = maxCheckoutTime;
    }

    public String getDefaultNetworkTimeout() {
        return defaultNetworkTimeout;
    }

    public void setDefaultNetworkTimeout(String defaultNetworkTimeout) {
        this.defaultNetworkTimeout = defaultNetworkTimeout;
    }

    public String getRetryTimeToWait() {
        return retryTimeToWait;
    }

    public void setRetryTimeToWait(String retryTimeToWait) {
        this.retryTimeToWait = retryTimeToWait;
    }

    public String getMaxLocalBadConnectionTolerance() {
        return maxLocalBadConnectionTolerance;
    }

    public void setMaxLocalBadConnectionTolerance(String maxLocalBadConnectionTolerance) {
        this.maxLocalBadConnectionTolerance = maxLocalBadConnectionTolerance;
    }

    public String getPingQueryContent() {
        return pingQueryContent;
    }

    public void setPingQueryContent(String pingQueryContent) {
        this.pingQueryContent = pingQueryContent;
    }

    public String getPingVerifyContent() {
        return pingVerifyContent;
    }

    public void setPingVerifyContent(String pingVerifyContent) {
        this.pingVerifyContent = pingVerifyContent;
    }

    public String getPingEnabled() {
        return pingEnabled;
    }

    public void setPingEnabled(String pingEnabled) {
        this.pingEnabled = pingEnabled;
    }

    public String getPingConnectionsNotUsed() {
        return pingConnectionsNotUsed;
    }

    public void setPingConnectionsNotUsed(String pingConnectionsNotUsed) {
        this.pingConnectionsNotUsed = pingConnectionsNotUsed;
    }

    @Override
    public String toString() {
        return "ServerMetadataConfigure{" +
                "serverIp='" + serverIp + '\'' +
                ", serverPort='" + serverPort + '\'' +
                ", maxActiveConnections='" + maxActiveConnections + '\'' +
                ", maxIdleConnections='" + maxIdleConnections + '\'' +
                ", maxCheckoutTime='" + maxCheckoutTime + '\'' +
                ", defaultNetworkTimeout='" + defaultNetworkTimeout + '\'' +
                ", retryTimeToWait='" + retryTimeToWait + '\'' +
                ", maxLocalBadConnectionTolerance='" + maxLocalBadConnectionTolerance + '\'' +
                ", pingQueryContent='" + pingQueryContent + '\'' +
                ", pingVerifyContent='" + pingVerifyContent + '\'' +
                ", pingEnabled='" + pingEnabled + '\'' +
                ", pingConnectionsNotUsed='" + pingConnectionsNotUsed + '\'' +
                '}';
    }
}
