package com.allinfinance.dev.socket.handler;

import com.allinfinance.dev.core.constant.CommonConstants;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author zhangyong
 */
public class AbstractMessageIOHandler implements IoHandler {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMessageIOHandler.class);

    private String appName;

    public String getAppName() {
        return appName;
    }

    public AbstractMessageIOHandler(String appName) {
        this.appName = appName;
    }

    @Override
    public void sessionCreated(IoSession session) {
        String clientIp = ((InetSocketAddress) (session.getRemoteAddress())).getAddress().getHostAddress();
        session.setAttribute(CommonConstants.KEY_SESSION_CLIENT_IP, clientIp);
        logger.info("接收连接已创建@{},clientIp: {}", session, clientIp);
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
    public void messageSent(IoSession session, Object message) {
        logger.info("已发送完毕@" + session);
    }

    @Override
    public void inputClosed(IoSession session) {
        logger.info("输入关闭@" + session);
        session.closeNow();
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        logger.info("超时关闭连接@" + session);
        session.closeNow();
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        logger.error("连接异常@" + session, cause);
        session.closeNow();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        //调用方自行实现
    }
}
