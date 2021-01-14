package com.allinfinance.dev.example.socket;

import com.allinfinance.dev.core.util.http.client.HttpConnectionManager;
import com.allinfinance.dev.socket.handler.AbstractMessageIOHandler;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class TestIOHandler extends AbstractMessageIOHandler {

    @Bean(initMethod = "init")
    public HttpConnectionManager httpConnectionManager() {
        HttpConnectionManager httpConnectionManager = new HttpConnectionManager();
        httpConnectionManager.setMaxPoolSize(50);
        httpConnectionManager.setCharSet("UTF-8");
        httpConnectionManager.setInitPoolSize(5);
        httpConnectionManager.setSocketSoTimeOut(30000);
        return httpConnectionManager;
    }

    private static final Logger logger = LoggerFactory.getLogger(AbstractMessageIOHandler.class);

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String reqMsg = (String) message;
        logger.info("收到请求消息:{}", reqMsg);
        String respMsg = "【服务端响应数据】" + reqMsg;
        logger.info("返回应答消息:{}|{}", respMsg, respMsg.getBytes(StandardCharsets.UTF_8).length);
        session.write(respMsg);
    }
}
