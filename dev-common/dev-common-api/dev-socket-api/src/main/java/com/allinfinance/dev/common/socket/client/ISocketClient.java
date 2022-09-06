package com.allinfinance.dev.common.socket.client;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/06 9:30
 */
public interface ISocketClient {
    /**
     * socket客户端请求
     *
     * @param remoteIp       目标ip
     * @param remotePort     目标端口
     * @param clientAppName  客户端名称（作为hashMap的key），例如：无卡非金融-qpsDiy，无卡金融：qps8583
     * @param timeOutSeconds 服务端超时时间
     * @param checkMac       是否检查mac
     * @param message        报文内容
     * @param msgLengthSize  报文长度
     * @param msgEncode      报文编码格式
     * @return 服务端返回报文
     */
    String clientRequest(String remoteIp, int remotePort, String clientAppName, int timeOutSeconds, boolean checkMac, String message, int msgLengthSize, String msgEncode);
}
