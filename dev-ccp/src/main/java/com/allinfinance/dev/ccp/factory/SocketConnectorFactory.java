package com.allinfinance.dev.ccp.factory;

import com.allinfinance.dev.core.util.socket.client.ClientIoHandler;
import com.allinfinance.dev.core.util.socket.codec.*;
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

    public NioSocketConnector registrar(String clientAppName, int timeout, int msgLengthSize, String msgEncode) {
        String key = spliceMapKey(clientAppName, timeout, msgLengthSize, msgEncode);
        NIO_SOCKET_CONNECTOR_MAP.computeIfAbsent(key, s -> {
            NioSocketConnector nioSocketConnector = new NioSocketConnector();
            nioSocketConnector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, timeout);
            nioSocketConnector.getSessionConfig().setUseReadOperation(true);
            nioSocketConnector.setConnectTimeoutMillis(timeout * 1000L);
            nioSocketConnector.setHandler(new ClientIoHandler(false));

            if (clientAppName.contains("8583")) {
                nioSocketConnector.getFilterChain().addLast(
                        "8583MsgCodec",
                        new ProtocolCodecFilter(new MessageCodecFactory(new Message8583Decoder(), new Message8583Encoder())));
            } else {
                nioSocketConnector.getFilterChain().addLast(
                        "diyMsgCodec", new ProtocolCodecFilter(new MessageCodecFactory(new DemuxingMessageDecoder(msgLengthSize, msgEncode),
                                new DemuxingMessageEncoder(msgLengthSize, msgEncode))));
            }
            return nioSocketConnector;
        });
        return getConnector(key);
    }

    public NioSocketConnector getConnector(String key) {
        return NIO_SOCKET_CONNECTOR_MAP.get(key);
    }

    /**
     * 根据请求字段拼接连接缓存的key
     *
     * @param clientAppName 应用名
     * @param timeout       超时时间，单位秒
     * @param msgLengthSize 长度域长度
     * @param msgEncode     编码方式
     * @return 拼接后的字符串
     */
    public String spliceMapKey(String clientAppName, int timeout, int msgLengthSize, String msgEncode) {
        return clientAppName + timeout + msgLengthSize + msgEncode;
    }
}
