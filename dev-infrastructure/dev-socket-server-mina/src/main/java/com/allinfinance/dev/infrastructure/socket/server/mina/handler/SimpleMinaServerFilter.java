package com.allinfinance.dev.infrastructure.socket.server.mina.handler;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huanghf
 * @date 2024/3/21 11:22
 */
public class SimpleMinaServerFilter extends IoFilterAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMinaServerFilter.class);

    @Override
    public void sessionCreated(NextFilter nextFilter, IoSession session) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("连接创建：{}", session);
        }
        super.sessionCreated(nextFilter, session);
    }

    @Override
    public void sessionOpened(NextFilter nextFilter, IoSession session) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("连接打开：{}", session);
        }
        super.sessionOpened(nextFilter, session);
    }

    @Override
    public void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("连接关闭：{}", session);
        }
        super.sessionClosed(nextFilter, session);
    }

    @Override
    public void sessionIdle(NextFilter nextFilter, IoSession session, IdleStatus status) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("连接{}", status);
        }
        super.sessionIdle(nextFilter, session, status);
    }
}
