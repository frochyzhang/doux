package com.allinfinance.dev.socket.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.allinfinance.dev.core.bean.MinaSocketBean;
import com.allinfinance.dev.core.util.socket.codec.MessageCodecFactory;
import com.allinfinance.dev.socket.conncheck.KeepAliveMessageFactoryImpl;
import com.allinfinance.dev.socket.conncheck.KeepAliveRequestTimeoutHandlerImpl;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageEncoder;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * @author 张勇
 * @date 2020-11-28 01:24
 */
@Configuration
public class ShortSwitchServer implements DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(ShortSwitchServer.class);

    private static final ConcurrentHashMap<Integer, IoAcceptor> IO_ACCEPTOR_MAP = new ConcurrentHashMap<>();

    @Autowired
    private List<MinaSocketBean> socketBeans;

    private static ThreadPoolExecutor threadPoolExecutor;

    /**
     * 根据SocketBeanLoader中解析配置文件获取到的配置信息，依次开启对应服务端口并监听
     *
     * @author 张勇
     * @date 2020-11-28 01:38
     */
    @Bean
    @Order
    public void init() {
        CountDownLatch countDownLatch = new CountDownLatch(socketBeans.size());
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNamePrefix("socket-server-pool-")
                .setDaemon(true)
                .build();
        threadPoolExecutor = new ThreadPoolExecutor(socketBeans.size(), socketBeans.size(), 0L,
                TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(1), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());

        logger.info("正在启动应用，请稍后!");
        socketBeans.forEach(minaSocketBean -> {
            threadPoolExecutor.submit(() -> {
                try {
                    initMinaServer(minaSocketBean);
                } catch (Exception e2) {
                    logger.error("[ {}] 启动服务失败! 参数为{}", minaSocketBean.getName(), minaSocketBean, e2);
                    System.exit(0);
                }

                countDownLatch.countDown();
                logger.info("{}-Server Thread start!", minaSocketBean.getName());
            });
        });
    }

    public void initMinaServer(MinaSocketBean minaSocketBean) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, IOException {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNamePrefix(minaSocketBean.getName() + "-pool-").build();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(minaSocketBean.getThreadCount(), minaSocketBean.getThreadCount(),
                0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(1), namedThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
        MessageDecoder messageDecoder = (MessageDecoder) Class.forName(minaSocketBean.getDecoderClassName())
                .getConstructor(Integer.class, String.class)
                .newInstance(minaSocketBean.getDecodeMsgLength(), minaSocketBean.getDecodeCharset());
        MessageEncoder messageEncoder = (MessageEncoder) Class.forName(minaSocketBean.getEncoderClassName())
                .getConstructor(Integer.class, String.class)
                .newInstance(minaSocketBean.getEncodeMsgLength(), minaSocketBean.getEncodeCharset());
        IoAcceptor acceptor = new NioSocketAcceptor(minaSocketBean.getProcessorCount());
        acceptor.getFilterChain().addLast("MsgCodec",
                new ProtocolCodecFilter(new MessageCodecFactory(messageDecoder, messageEncoder)));
        acceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(threadPoolExecutor));
        acceptor.setHandler((IoHandler) Class.forName(minaSocketBean.getHandlerClassName()).getConstructor(String.class)
                .newInstance(minaSocketBean.getName()));
        acceptor.getSessionConfig().setReadBufferSize(minaSocketBean.getBufferSize());
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, minaSocketBean.getTimeOut());
        // TODO: 2021/3/22 长链接处理逻辑
        if (minaSocketBean.getKeepAlive()) {
            logger.info("开启服务端保持连接!");
            KeepAliveFilter keepAliveFilter = new KeepAliveFilter(new KeepAliveMessageFactoryImpl(),
                    IdleStatus.BOTH_IDLE, new KeepAliveRequestTimeoutHandlerImpl());
            keepAliveFilter.setForwardEvent(true);
            keepAliveFilter.setRequestInterval(minaSocketBean.getBeatInterval());
            keepAliveFilter.setRequestTimeout(minaSocketBean.getBeatTimeout());
            acceptor.getFilterChain().addLast("heartbeat", keepAliveFilter);
        } else {
            logger.info("关闭服务端保持连接!");
            acceptor.setCloseOnDeactivation(true);
            if (minaSocketBean.getSoLinger()) {
                logger.info("TIME_WAIT SO_LINGER = 0生效!");
                SocketSessionConfig sessionConfig = (SocketSessionConfig) acceptor.getSessionConfig();
                sessionConfig.setSoLinger(0);
            }
        }

        acceptor.bind(new InetSocketAddress(minaSocketBean.getPort()));
        IO_ACCEPTOR_MAP.putIfAbsent(minaSocketBean.getPort(), acceptor);
        logger.info("[ {} ] 服务端启动成功! 参数为{}", minaSocketBean.getName(), minaSocketBean);
    }

    /**
     * Invoked by the containing {@code BeanFactory} on destruction of a bean.
     */
    @Override
    public void destroy() {
        threadPoolExecutor.shutdown();
        logger.info("socket server thread pool is shutting down!");
    }

    public static void closeMinaServer(int port) {
        Optional.ofNullable(IO_ACCEPTOR_MAP.get(port))
                .ifPresent(ioAcceptor -> {
                    ioAcceptor.unbind();
                    IO_ACCEPTOR_MAP.remove(port);
                });
        logger.info("Mina Server Has Been Shutdown ............");
    }

    public static IoAcceptor getInstance(int port) {
        return IO_ACCEPTOR_MAP.get(port);
    }
}
