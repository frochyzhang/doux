package com.allinfinance.dev.socket.conncheck;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classname  com.allinfinance.dev.socket.conncheck.KeepAliveRequestTimeoutHandlerImpl
 *
 * @Description 处理心跳超时
 * @Date 2021/3/22 17:10
 * @Created by ZhangYong
 */
public class KeepAliveRequestTimeoutHandlerImpl implements KeepAliveRequestTimeoutHandler {
    private static final Logger logger = LoggerFactory.getLogger(KeepAliveRequestTimeoutHandlerImpl.class);

    @Override
    public void keepAliveRequestTimedOut(KeepAliveFilter keepAliveFilter, IoSession ioSession) throws Exception {
        logger.error("心跳超时");
    }
}
