package com.allinfinance.dev.infrastructure.socket.server.mina;

import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.util.StrUtil;
import com.allinfinance.dev.framework.extension.annotation.Extension;
import com.allinfinance.dev.framework.socket.server.driver.SocketServer;
import com.allinfinance.dev.infrastructure.socket.server.mina.coder.DemuxingMessageDecoder;
import com.allinfinance.dev.infrastructure.socket.server.mina.coder.DemuxingMessageEncoder;
import com.allinfinance.dev.infrastructure.socket.server.mina.coder.MessageCodecFactory;
import com.allinfinance.dev.infrastructure.socket.server.mina.handler.SimpleMinaServerFilter;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageEncoder;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author huanghf
 * @date 2024/3/21 11:27
 */
@Extension("mina")
public class MinaSocketServer implements SocketServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinaSocketServer.class);

    private static final ConcurrentHashMap<Integer, IoAcceptor> IO_ACCEPTOR_MAP = new ConcurrentHashMap<>();

    @Override
    public void start(Properties properties) {
        LOGGER.info("开始初始化Mina服务端");
        String name = properties.getProperty("name");
        int port = Integer.parseInt(properties.getProperty("port"));
        String encodeCharset = properties.getProperty("encodeCharset");
        String decodeCharset = properties.getProperty("decodeCharset");
        String encoderClassName = properties.getProperty("encoderClassName");
        String decoderClassName = properties.getProperty("decoderClassName");
        String filterClassName = properties.getProperty("filterClassName");
        String handlerClassName = properties.getProperty("handlerClassName");
        int readerIdleTime = Integer.parseInt(properties.getProperty("readerIdleTime"));
        int writerIdleTime = Integer.parseInt(properties.getProperty("writerIdleTime"));
        int decodeMsgLength = Integer.parseInt(properties.getProperty("decodeMsgLength"));
        int encodeMsgLength = Integer.parseInt(properties.getProperty("encodeMsgLength"));

        IoAcceptor acceptor = new NioSocketAcceptor();
        if (StrUtil.isNotEmpty(encoderClassName) && StrUtil.isNotEmpty(decoderClassName)) {
            try {
                MessageEncoder messageEncoder = (MessageEncoder) Class.forName(encoderClassName)
                        .getConstructor(Integer.class, String.class)
                        .newInstance(encodeMsgLength, encodeCharset);
                MessageDecoder messageDecoder = (MessageDecoder) Class.forName(decoderClassName)
                        .getConstructor(Integer.class, String.class)
                        .newInstance(decodeMsgLength, decodeCharset);
                acceptor.getFilterChain().addLast(
                        "codec",
                        new ProtocolCodecFilter(new MessageCodecFactory(messageDecoder, messageEncoder))
                );
            } catch (Exception e) {
                LOGGER.error("初始化coder失败，encoder: {}, decoder: {}", encoderClassName, decoderClassName, e);
                throw new RuntimeException("初始化coder失败，encoder: " + encoderClassName + ", decoder: " + decoderClassName);
            }
        } else {
            acceptor.getFilterChain().addLast(
                    "codec",
                    new ProtocolCodecFilter(
                            new MessageCodecFactory(
                                    new DemuxingMessageDecoder(decodeMsgLength, decodeCharset),
                                    new DemuxingMessageEncoder(encodeMsgLength, encodeCharset)
                            )
                    )
            );
        }
        ThreadFactory threadFactory = new NamedThreadFactory("mina-server-pool-", false);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(), 0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
        acceptor.getFilterChain().addLast("minaHandlerExecutor", new ExecutorFilter(executor));
        acceptor.getFilterChain().addLast("simpleMinaServerFilter", new SimpleMinaServerFilter());
        StrUtil.split(filterClassName, ",", true, true)
                .forEach(className -> {
                    try {
                        IoFilter ioFilter = (IoFilter) Class.forName(className)
                                .getConstructor()
                                .newInstance();
                        acceptor.getFilterChain().addLast(className, ioFilter);
                    } catch (Exception e) {
                        LOGGER.error("初始化filter失败，filterClassName：{}", className, e);
                        throw new RuntimeException("初始化filter异常");
                    }
                });
        try {
            IoHandler ioHandler = (IoHandler) Class.forName(handlerClassName)
                    .getConstructor()
                    .newInstance();
            acceptor.setHandler(ioHandler);
        } catch (Exception e) {
            LOGGER.error("初始化handler失败，handlerClassName：{}", handlerClassName, e);
            throw new RuntimeException("初始化handler异常");
        }
        acceptor.getSessionConfig().setReaderIdleTime(readerIdleTime);
        acceptor.getSessionConfig().setWriterIdleTime(writerIdleTime);
        acceptor.setCloseOnDeactivation(true);
        try {
            acceptor.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            LOGGER.error("[{}]服务启动失败，启动参数：{}", name, properties, e);
            throw new RuntimeException("Mina Server服务启动失败");
        }
        LOGGER.info("Mina server [{}] started on port: {}", name, port);
        IO_ACCEPTOR_MAP.putIfAbsent(port, acceptor);
    }

    @Override
    public void close(Integer port) {
        LOGGER.info("开始关闭端口：{}", port);
        Optional.ofNullable(IO_ACCEPTOR_MAP.get(port))
                .ifPresent(ioAcceptor -> {
                    ioAcceptor.unbind();
                    ioAcceptor.dispose();
                    IO_ACCEPTOR_MAP.remove(port);
                    LOGGER.info("Mina socket server on port {} has been shutdown ...", port);
                });
    }
}
