package com.allinfinance.dev.ccp.factory;

import com.allinfinance.dev.core.util.socket.client.ClientIoHandler;
import com.allinfinance.dev.core.util.socket.codec.DemuxingMessageDecoder;
import com.allinfinance.dev.core.util.socket.codec.DemuxingMessageEncoder;
import com.allinfinance.dev.core.util.socket.codec.MessageCodecFactory;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/15 17:26
 */
@Component
public class SocketConnectorFactory {
    private static final Map<String, NioSocketConnector> NIO_SOCKET_CONNECTOR_MAP = new ConcurrentHashMap<>();

    public NioSocketConnector registrar(String clientAppName, int timeOut, int msgLengthSize, String msgEncode) {
        NIO_SOCKET_CONNECTOR_MAP.computeIfAbsent(clientAppName, s -> {
            NioSocketConnector nioSocketConnector = new NioSocketConnector();
            nioSocketConnector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, timeOut);
            nioSocketConnector.getSessionConfig().setUseReadOperation(true);
            nioSocketConnector.setConnectTimeoutMillis(timeOut * 1000L);
            nioSocketConnector.setHandler(new ClientIoHandler(false));

            nioSocketConnector.getFilterChain().addLast(
                    "msgCodec", new ProtocolCodecFilter(new MessageCodecFactory(new DemuxingMessageDecoder(msgLengthSize, msgEncode),
                            new DemuxingMessageEncoder(msgLengthSize, msgEncode))));
            return nioSocketConnector;
        });
        return getConnector(clientAppName);
    }

    public NioSocketConnector getConnector(String clientAppName) {
        return NIO_SOCKET_CONNECTOR_MAP.get(clientAppName);
    }
}
