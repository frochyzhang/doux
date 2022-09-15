package com.allinfinance.dev.common.socket.client.pojo;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/08 10:40
 */
public class SocketRequestDTO {
    /**
     * 目标ip
     */
    private String remoteIp;
    /**
     * 目标端口
     */
    private String remotePort;
    /**
     * 客户端名称（作为hashMap的key），例如：无卡非金融-qpsDiy，无卡金融：qps8583
     */
    private String clientAppName;
    /**
     * 服务端超时时间
     */
    private String timeOutSeconds = "30";
    /**
     * 是否检查mac
     */
    private String checkMac = "false";
    /**
     * 报文长度
     */
    private String msgLengthSize = "6";
    /**
     * 报文编码格式
     */
    private String msgEncode = "UTF-8";
    /**
     * 底层实现的选择
     */
    private String connectionDriver = "socketNetty";
    /**
     * 客户端实现
     */
    private String socketClient = "default";

    public SocketRequestDTO() {
    }

    public SocketRequestDTO(String remoteIp, String remotePort, String clientAppName) {
        this.remoteIp = remoteIp;
        this.remotePort = remotePort;
        this.clientAppName = clientAppName;
    }

    public SocketRequestDTO(String remoteIp, String remotePort, String clientAppName, String msgLengthSize, String msgEncode) {
        this.remoteIp = remoteIp;
        this.remotePort = remotePort;
        this.clientAppName = clientAppName;
        this.msgLengthSize = msgLengthSize;
        this.msgEncode = msgEncode;
    }

    public String getConnectionDriver() {
        return connectionDriver;
    }

    public void setConnectionDriver(String connectionDriver) {
        this.connectionDriver = connectionDriver;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public String getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(String remotePort) {
        this.remotePort = remotePort;
    }

    public String getClientAppName() {
        return clientAppName;
    }

    public void setClientAppName(String clientAppName) {
        this.clientAppName = clientAppName;
    }

    public String getTimeOutSeconds() {
        return timeOutSeconds;
    }

    public void setTimeOutSeconds(String timeOutSeconds) {
        this.timeOutSeconds = timeOutSeconds;
    }

    public String getCheckMac() {
        return checkMac;
    }

    public void setCheckMac(String checkMac) {
        this.checkMac = checkMac;
    }

    public String getMsgLengthSize() {
        return msgLengthSize;
    }

    public void setMsgLengthSize(String msgLengthSize) {
        this.msgLengthSize = msgLengthSize;
    }

    public String getMsgEncode() {
        return msgEncode;
    }

    public void setMsgEncode(String msgEncode) {
        this.msgEncode = msgEncode;
    }

    public String getSocketClient() {
        return socketClient;
    }

    public void setSocketClient(String socketClient) {
        this.socketClient = socketClient;
    }

    @Override
    public String toString() {
        return "SocketRequestDTO{" +
                "remoteIp='" + remoteIp + '\'' +
                ", remotePort=" + remotePort +
                ", clientAppName='" + clientAppName + '\'' +
                ", timeOutSeconds=" + timeOutSeconds +
                ", checkMac=" + checkMac +
                ", msgLengthSize=" + msgLengthSize +
                ", msgEncode='" + msgEncode + '\'' +
                ", socketClient='" + socketClient + '\'' +
                '}';
    }
}
