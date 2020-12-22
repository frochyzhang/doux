package com.allinfinance.dev.socket.handler;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractMessageIOHandler extends IoHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMessageIOHandler.class);

    @Override
    public void sessionCreated(IoSession session) {
        logger.info("接收连接已创建@" + session);
    }

    @Override
    public void sessionOpened(IoSession session) {
        logger.info("接收连接已打开@" + session);
    }

    @Override
    public void sessionClosed(IoSession session) {
        logger.info("连接关闭@" + session);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        logger.info("message:{}" + "已发送完毕@" + session, message);
    }

    @Override
    public void inputClosed(IoSession session) throws Exception {
        logger.info("输入关闭@" + session);
        super.inputClosed(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        logger.info("超时关闭连接@" + session);
        session.closeNow();
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        logger.error("连接异常@" + session, cause);
        super.exceptionCaught(session, cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
    }
}
