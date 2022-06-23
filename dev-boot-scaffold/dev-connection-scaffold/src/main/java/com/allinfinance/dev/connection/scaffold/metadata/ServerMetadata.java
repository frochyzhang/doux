package com.allinfinance.dev.connection.scaffold.metadata;

import com.yuyee.dev.connection.scaffold.config.constant.ConnectionStatus;
import com.yuyee.dev.connection.scaffold.netty.connection.ClientConnection;

/**
 * @author qipeng
 * @date 2022/6/14 17:19
 */
public class ServerMetadata {
    /**
     * 服务端名称
     */
    private String serverName;
    /**
     * 服务端ip
     */
    private String serverIp;
    /**
     * 服务端port
     */
    private Integer serverPort;
    /**
     * 超时时间，单位：毫秒
     */
    private Integer timeout;
    /**
     * 连接检查时间，单位：毫秒
     */
    private Integer checkoutTime;
    /**
     * 重试次数
     */
    private Integer retryTimes;
    /**
     * 最大活跃连接数
     */
    private Integer maxActiveConnection;
    /**
     * 最大空闲连接数
     */
    private Integer maxIdleConnection;
    /**
     * ping检查开关
     */
    private Boolean enablePing;
    /**
     * ping报文请求内容
     */
    private String pingQueryMessage;
    /**
     * ping报文校验内容
     */
    private String pingVerifyMessage;

    /**
     * 获取连接
     *
     * @return
     */
    public ClientConnection fetchConnection() {
        ClientConnection clientConnection = new ClientConnection();
        clientConnection.connect(serverIp, serverPort, retryTimes);
        clientConnection.setStatus(ConnectionStatus.ACTIVE);
        return clientConnection;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Integer getMaxActiveConnection() {
        return maxActiveConnection;
    }

    public void setMaxActiveConnection(Integer maxActiveConnection) {
        this.maxActiveConnection = maxActiveConnection;
    }

    public Integer getMaxIdleConnection() {
        return maxIdleConnection;
    }

    public void setMaxIdleConnection(Integer maxIdleConnection) {
        this.maxIdleConnection = maxIdleConnection;
    }

    public String getPingQueryMessage() {
        return pingQueryMessage;
    }

    public void setPingQueryMessage(String pingQueryMessage) {
        this.pingQueryMessage = pingQueryMessage;
    }

    public String getPingVerifyMessage() {
        return pingVerifyMessage;
    }

    public void setPingVerifyMessage(String pingVerifyMessage) {
        this.pingVerifyMessage = pingVerifyMessage;
    }

    public Boolean getEnablePing() {
        return enablePing;
    }

    public void setEnablePing(Boolean enablePing) {
        this.enablePing = enablePing;
    }

    public Integer getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(Integer checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    @Override
    public String toString() {
        return "ServerMetadata{" +
                "serverName='" + serverName + '\'' +
                ", serverIp='" + serverIp + '\'' +
                ", serverPort=" + serverPort +
                ", timeout=" + timeout +
                ", checkoutTime=" + checkoutTime +
                ", retryTimes=" + retryTimes +
                ", maxActiveConnection=" + maxActiveConnection +
                ", maxIdleConnection=" + maxIdleConnection +
                ", enablePing=" + enablePing +
                ", pingQueryMessage='" + pingQueryMessage + '\'' +
                ", pingVerifyMessage='" + pingVerifyMessage + '\'' +
                '}';
    }
}
