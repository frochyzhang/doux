package com.allinfinance.dev.core.util.socket.client;

/**
 * ISocketService
 *
 * @author hongmr
 * @date 2017/2/24
 */
public interface ISocketService {
    String clientRequest(String remoteIp, int remotePort, String format, int timeOut, boolean checkMac, String message, int msgLengthSize, String msgEncode);
}
