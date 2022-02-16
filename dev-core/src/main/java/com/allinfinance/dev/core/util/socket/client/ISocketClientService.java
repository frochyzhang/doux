package com.allinfinance.dev.core.util.socket.client;

/**
 * ISocketService
 *
 * @author hongmr
 * @date 2017/2/24
 */
public interface ISocketClientService {
    String clientRequest(String remoteIp, int remotePort, String clientAppName, int timeOutSeconds, boolean checkMac, String message, int msgLengthSize, String msgEncode);
}
