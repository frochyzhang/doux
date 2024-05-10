package cn.lezoo.doux.socket.server.scaffold.service;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.lezoo.doux.framework.extension.loader.ExtensionLoaderFactory;
import cn.lezoo.doux.framework.socket.server.driver.SocketServerWrapper;
import cn.lezoo.doux.socket.server.scaffold.api.SocketServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022-09-16 15:16
 */
@Component
@ConditionalOnProperty(prefix = "cn.lezoo.socket.server.bootstrap", name = "enabled", havingValue = "true")
public class SocketServerServiceImpl implements SocketServerService, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(SocketServerServiceImpl.class);

    @Autowired
    @Qualifier("socketServerList")
    private Map<String, List<Properties>> socketServerList;

    /**
     * 根据传入的socket beans开启多端口监听
     */
    @Override
    public void start() {
        if (logger.isDebugEnabled()) {
            logger.debug("启动socketServer多端口服务");
        }
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNamePrefix("netty-server-pool-")
                .setDaemon(true)
                .build();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(socketServerList.size(), socketServerList.size(), 0L,
                TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(1), threadFactory);
        socketServerList.forEach((bootstrap, propertiesList) -> {
            threadPoolExecutor.execute(() -> {
                SocketServerWrapper socketServer = ExtensionLoaderFactory.getExtension(SocketServerWrapper.class, bootstrap);
                socketServer.start(propertiesList);
            });
        });
    }

    @Override
    public void close(Integer port) {
        socketServerList.keySet()
                .forEach(bootstrap -> ExtensionLoaderFactory.getExtension(SocketServerWrapper.class, bootstrap).close(port));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    @Override
    public void destroy() throws Exception {
        socketServerList.keySet()
                .forEach(bootstrap -> ExtensionLoaderFactory.getExtension(SocketServerWrapper.class, bootstrap).closeAll());
    }
}
