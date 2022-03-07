package com.allinfinance.dev.ccp.service;

import com.allinfinance.dev.ccp.factory.SocketConnectorFactory;
import com.allinfinance.dev.core.util.socket.client.ISocketClientService;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/15 15:37
 */
@Service
public class SocketClientServiceImpl implements ISocketClientService {
    private static final Logger logger = LoggerFactory.getLogger(SocketClientServiceImpl.class);
    @Autowired
    private SocketConnectorFactory socketConnectorFactory;

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
    @Override
    public String clientRequest(String remoteIp, int remotePort, String clientAppName, int timeOutSeconds, boolean checkMac, String message, int msgLengthSize, String msgEncode) {

        NioSocketConnector nioSocketConnector = socketConnectorFactory.getConnector(clientAppName);
        if (nioSocketConnector == null) {
            nioSocketConnector = socketConnectorFactory.registrar(clientAppName, timeOutSeconds, msgLengthSize, msgEncode);
        }

        ConnectFuture future = nioSocketConnector.connect(new InetSocketAddress(remoteIp, remotePort));
        future.awaitUninterruptibly();

        if (!future.isConnected()) {
            logger.error("与server建立socket连接失败,ip:{},port:{}", remoteIp, remotePort);
        }
        IoSession session = null;
        try {
            session = future.getSession();
            session.write(message);
            ReadFuture readFuture = session.read();

            String resp = null;
            if (readFuture.awaitUninterruptibly(timeOutSeconds, TimeUnit.SECONDS)) {
                Object respMess = readFuture.getMessage();
                if (respMess != null) {
                    resp = (String) respMess;
                }
            } else {
                logger.error("读取应答消息失败.");
            }
            return resp;
        } finally {
            if (session != null) {
                session.closeNow();
            }
        }

    }
}
