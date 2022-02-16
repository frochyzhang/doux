package com.allinfinance.dev.example.socket;

import com.allinfinance.dev.example.socket.factory.AppProcessFactory;
import com.allinfinance.dev.socket.handler.AbstractMessageIOHandler;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class TestIOHandler extends AbstractMessageIOHandler {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMessageIOHandler.class);

    public TestIOHandler(String appName) {
        super(appName);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String reqMsg = (String) message;
        logger.info("收到请求消息:{}", reqMsg);
        String respMsg = "AppProcessFactory.processed(this.getAppName(), reqMsg)";
        logger.info("返回应答消息:{}|{}", respMsg, respMsg.getBytes(StandardCharsets.UTF_8).length);

        session.write(respMsg);
    }
}
