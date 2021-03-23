package com.allinfinance.dev.socket.conncheck;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classname  com.allinfinance.dev.socket.conncheck.KeepAliveMessageFactoryImpl
 *
 * @Description 心跳处理
 * @Date 2021/3/22 17:03
 * @Created by ZhangYong
 */
public class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {
    private static final Logger logger = LoggerFactory.getLogger(KeepAliveMessageFactoryImpl.class);

    private static final String HEARTBEAT_REQUEST = "heartBeatRequest";
    private static final String HEARTBEAT_RESPONSE = "heartBeatResponse";

    @Override
    public boolean isRequest(IoSession ioSession, Object o) {
        String msg = (String) o;
        if (msg.contains(HEARTBEAT_REQUEST)) {
            logger.info("心跳请求信息:{}", msg);
            return Boolean.TRUE;
        }
        return false;
    }

    @Override
    public boolean isResponse(IoSession ioSession, Object o) {
        String msg = (String) o;
        if (msg.contains(HEARTBEAT_RESPONSE)) {
            logger.info("心跳响应信息:{}", msg);
            return Boolean.TRUE;
        }
        return false;
    }

    @Override
    public Object getRequest(IoSession ioSession) {
        logger.info("预设请求心跳信息:{}", HEARTBEAT_REQUEST);
        return HEARTBEAT_REQUEST;
    }

    @Override
    public Object getResponse(IoSession ioSession, Object o) {
        logger.info("预设响应心跳信息:{}", HEARTBEAT_RESPONSE);
        return HEARTBEAT_RESPONSE;
    }
}
