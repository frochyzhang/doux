package com.allinfinance.dev.infrastructure.socket.client.mina.socket;

import com.allinfinance.dev.infrastructure.socket.client.mina.socket.codec.*;
import com.allinfinance.dev.infrastructure.socket.client.mina.socket.handler.ClientIoHandler;
import com.allinfinance.dev.framework.extension.annotation.Extension;
import com.allinfinance.dev.framework.socket.client.driver.Connection;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author qipeng
 * @date 2022/12/30 10:02
 * @desc
 */
@Extension("mina")
public class SocketMinaConnection implements Connection {
    private static final Logger logger = LoggerFactory.getLogger(SocketMinaConnection.class);

    IoSession session = null;
    NioSocketConnector clientConnector = null;
    int timeout;

    /**
     * 客户端发送请求
     *
     * @param msg 请求信息
     * @return 服务端响应
     */
    @Override
    public String send(String msg) {
        String resp;
        try {
            session.write(msg);
            resp = null;
            ReadFuture readFuture = session.read();
            if (readFuture.awaitUninterruptibly(timeout, TimeUnit.SECONDS)) {
                Object respMess = readFuture.getMessage();
                if (respMess != null) {
                    resp = (String) respMess;
                }
            } else {
                logger.error("读取应答消息失败.");
            }
        } finally {
            if (session != null) {
                session.closeNow();
                session.getService().dispose();
            }
            if (clientConnector != null) {
                clientConnector.dispose();
            }
        }
        return resp;
    }

    /**
     * 建立socket连接
     *
     * @param properties 客户端配置连接参数
     */
    @Override
    public void connect(Properties properties) {
        String serverIp = properties.getProperty("remoteIp");
        int serverPort = Integer.parseInt(properties.getProperty("remotePort"));
        int msgLengthSize = Integer.parseInt(properties.getProperty("msgLengthSize"));
        String msgEncode = properties.getProperty("msgEncode");
        String clientAppName = properties.getProperty("clientAppName");
        timeout = Integer.parseInt(properties.getProperty("timeout"));
        boolean checkMac = Boolean.parseBoolean(properties.getProperty("checkMac"));
        try {
            clientConnector = new NioSocketConnector();
            clientConnector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, timeout / 1000);
            if ("8583".equals(clientAppName)) {
                clientConnector.getFilterChain().addLast(
                        "8583MsgCodec",
                        new ProtocolCodecFilter(new MessageCodecFactory(new Message8583Decoder(), new Message8583Encoder())));
            } else {
                clientConnector.getFilterChain().addLast(
                        "diyMsgCodec",
                        new ProtocolCodecFilter(new MessageCodecFactory(new DemuxingMessageDecoder(msgLengthSize, msgEncode), new DemuxingMessageEncoder(msgLengthSize, msgEncode))));
            }
            clientConnector.setHandler(new ClientIoHandler(checkMac));
            clientConnector.getSessionConfig().setUseReadOperation(true);
            clientConnector.setConnectTimeoutMillis(timeout);
            ConnectFuture future = clientConnector
                    .connect(new InetSocketAddress(serverIp, serverPort));
            future.awaitUninterruptibly();
            if (!future.isConnected()) {
                logger.error("与server建立socket连接失败,ip:{},port:{}", serverIp, serverPort);
                clientConnector.dispose();
                throw new RuntimeException("与server建立socket连接失败");
            }
            session = future.getSession();
        } catch (Exception e) {
            logger.error("与server建立连接异常！", e);
        }
    }
}
