package com.allinfinance.dev.core.util.socket.codec.client;


import com.allinfinance.dev.core.util.socket.codec.*;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * SocketService
 * mina socket
 *
 * @author hongmr
 * @date 2017/1/5
 */
@Service("socketService")
public class SocketService implements ISocketService {
    private final Logger logger = LoggerFactory.getLogger(SocketService.class);

    @Override
    public String clientRequest(String remoteIp, int remotePort, String format, int timeOut, boolean checkMac, String message, int msgLengthSize, String msgEncode) {
        String resp = null;
        IoSession session = null;
        NioSocketConnector clientConnector = null;
        try {
            clientConnector = new NioSocketConnector();
            clientConnector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, timeOut);
            if ("8583".equals(format)) {
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
            clientConnector.setConnectTimeoutMillis(timeOut * 1000);
            ConnectFuture future = clientConnector
                    .connect(new InetSocketAddress(remoteIp, remotePort));
            future.awaitUninterruptibly();
            if (!future.isConnected()) {
                logger.error("与server建立socket连接失败,ip:{},port:{}", remoteIp, remotePort);
                clientConnector.dispose();
                throw new RuntimeException("与server建立socket连接失败");
            }
            session = future.getSession();
            session.write(message);
            ReadFuture readFuture = session.read();
            if (readFuture.awaitUninterruptibly(timeOut, TimeUnit.SECONDS)) {
                Object RespMess = readFuture.getMessage();
                if (RespMess != null)
                    resp = (String) RespMess;
            } else
                logger.error("读取应答消息失败.");
            return resp;
        } catch (RuntimeException e) {
            logger.error("与server通讯异常");
            return null;
        } finally {
            if (session != null) {
                session.closeNow();
                session.getService().dispose();
            }
            if (clientConnector != null)
                clientConnector.dispose();
        }
    }
}
