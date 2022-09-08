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
    private int remotePort;
    /**
     * 客户端名称（作为hashMap的key），例如：无卡非金融-qpsDiy，无卡金融：qps8583
     */
    private String clientAppName;
    /**
     * 服务端超时时间
     */
    private int timeOutSeconds;
    /**
     * 是否检查mac
     */
    private boolean checkMac;
    /**
     * 报文内容
     */
    private String message;
    /**
     * 报文长度
     */
    private int msgLengthSize;
    /**
     * 报文编码格式
     */
    private String msgEncode;
    /**
     * 目标端口
     */
    // todo
    private Boolean keepAlive;

    public SocketRequestDTO() {
    }

    public SocketRequestDTO(String remoteIp, int remotePort, String clientAppName, int timeOutSeconds, boolean checkMac, String message, int msgLengthSize, String msgEncode, Boolean keepAlive) {
        this.remoteIp = remoteIp;
        this.remotePort = remotePort;
        this.clientAppName = clientAppName;
        this.timeOutSeconds = timeOutSeconds;
        this.checkMac = checkMac;
        this.message = message;
        this.msgLengthSize = msgLengthSize;
        this.msgEncode = msgEncode;
        this.keepAlive = keepAlive;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public String getClientAppName() {
        return clientAppName;
    }

    public void setClientAppName(String clientAppName) {
        this.clientAppName = clientAppName;
    }

    public int getTimeOutSeconds() {
        return timeOutSeconds;
    }

    public void setTimeOutSeconds(int timeOutSeconds) {
        this.timeOutSeconds = timeOutSeconds;
    }

    public boolean isCheckMac() {
        return checkMac;
    }

    public void setCheckMac(boolean checkMac) {
        this.checkMac = checkMac;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMsgLengthSize() {
        return msgLengthSize;
    }

    public void setMsgLengthSize(int msgLengthSize) {
        this.msgLengthSize = msgLengthSize;
    }

    public String getMsgEncode() {
        return msgEncode;
    }

    public void setMsgEncode(String msgEncode) {
        this.msgEncode = msgEncode;
    }

    public Boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    @Override
    public String toString() {
        return "SocketRequestDTO{" +
                "remoteIp='" + remoteIp + '\'' +
                ", remotePort=" + remotePort +
                ", clientAppName='" + clientAppName + '\'' +
                ", timeOutSeconds=" + timeOutSeconds +
                ", checkMac=" + checkMac +
                ", message='" + message + '\'' +
                ", msgLengthSize=" + msgLengthSize +
                ", msgEncode='" + msgEncode + '\'' +
                ", keepAlive=" + keepAlive +
                '}';
    }
}
