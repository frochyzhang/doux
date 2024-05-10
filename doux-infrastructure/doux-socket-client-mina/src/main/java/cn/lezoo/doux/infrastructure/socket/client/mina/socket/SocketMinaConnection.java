package cn.lezoo.doux.infrastructure.socket.client.mina.socket;

import cn.lezoo.doux.framework.extension.annotation.Extension;
import cn.lezoo.doux.framework.socket.client.driver.Connection;
import cn.lezoo.doux.infrastructure.socket.client.mina.socket.codec.DemuxingMessageDecoder;
import cn.lezoo.doux.infrastructure.socket.client.mina.socket.codec.DemuxingMessageEncoder;
import cn.lezoo.doux.infrastructure.socket.client.mina.socket.codec.MessageCodecFactory;
import cn.lezoo.doux.infrastructure.socket.client.mina.socket.handler.ClientIoHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageEncoder;
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

    private static final ThreadLocal<ConnectFuture> CONNECT_FUTURE = new ThreadLocal<>();

    private int timeout;

    /**
     * 客户端发送请求
     *
     * @param msg 请求信息
     * @return 服务端响应
     */
    @Override
    public String send(String msg) {
        IoSession session = CONNECT_FUTURE.get().getSession();
        CONNECT_FUTURE.remove();
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
            // if (clientConnector != null) {
            //     clientConnector.dispose();
            // }
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
        String encoderClassName = properties.getProperty("encoderClassName");
        String decoderClassName = properties.getProperty("decoderClassName");
        timeout = Integer.parseInt(properties.getProperty("timeout"));
        boolean checkMac = Boolean.parseBoolean(properties.getProperty("checkMac"));
        IoConnector connector = null;
        try {
            connector = new NioSocketConnector();
            connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, timeout / 1000);
            if (StringUtils.isNotEmpty(encoderClassName) && StringUtils.isNotEmpty(decoderClassName)) {
                try {
                    MessageEncoder messageEncoder = (MessageEncoder) Class.forName(encoderClassName)
                            .getConstructor(Integer.class, String.class)
                            .newInstance(msgLengthSize, msgEncode);
                    MessageDecoder messageDecoder = (MessageDecoder) Class.forName(decoderClassName)
                            .getConstructor(Integer.class, String.class)
                            .newInstance(msgLengthSize, msgEncode);
                    connector.getFilterChain().addLast(
                            "codec",
                            new ProtocolCodecFilter(new MessageCodecFactory(messageDecoder, messageEncoder))
                    );
                } catch (Exception e) {
                    logger.error("初始化coder失败，encoder: {}, decoder: {}", encoderClassName, decoderClassName, e);
                    throw new RuntimeException("初始化coder失败，encoder: " + encoderClassName + ", decoder: " + decoderClassName);
                }
            } else {
                connector.getFilterChain().addLast(
                        "codec",
                        new ProtocolCodecFilter(
                                new MessageCodecFactory(
                                        new DemuxingMessageDecoder(msgLengthSize, msgEncode),
                                        new DemuxingMessageEncoder(msgLengthSize, msgEncode)
                                )
                        )
                );
            }
            connector.setHandler(new ClientIoHandler(checkMac));
            connector.getSessionConfig().setUseReadOperation(true);
            connector.setConnectTimeoutMillis(timeout);
            ConnectFuture future = connector.connect(new InetSocketAddress(serverIp, serverPort));
            future.awaitUninterruptibly();
            if (!future.isConnected()) {
                logger.error("与server建立socket连接失败,ip:{},port:{}", serverIp, serverPort);
                connector.dispose();
                throw new RuntimeException("与server建立socket连接失败");
            }
            CONNECT_FUTURE.set(future);
        } catch (Exception e) {
            logger.error("与server建立连接异常！", e);
        }
    }
}
