package com.allinfinance.dev.framework.socket.server.wrapper;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.allinfinance.dev.framework.extension.annotation.Extension;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoader;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;
import com.allinfinance.dev.framework.socket.server.driver.SocketServer;
import com.allinfinance.dev.framework.socket.server.driver.SocketServerWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022-09-16 09:49
 */
@Extension("default")
public class DefaultSocketServerWrapper implements SocketServerWrapper {

    private static final Logger logger = LoggerFactory.getLogger(DefaultSocketServerWrapper.class);

    private static ThreadPoolExecutor threadPoolExecutor;

    private SocketServer socketServer;

    /**
     * 根据配置开启服务端口
     *
     * @param propertyList 服务端配置集合
     */
    @Override
    public void start(List<Properties> propertyList) {
        ExtensionLoader<SocketServer> loader = ExtensionLoaderFactory.getExtensionLoader(SocketServer.class);
        CountDownLatch countDownLatch = new CountDownLatch(propertyList.size());
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNamePrefix("netty-server-pool-")
                .setDaemon(true)
                .build();
        threadPoolExecutor = new ThreadPoolExecutor(propertyList.size(), propertyList.size(), 0L,
                TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(1), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
        logger.info("正在启动应用，请稍后!");
        propertyList.forEach(properties -> threadPoolExecutor.submit(() -> {
            try {
                String serverDriver = properties.getProperty("serverDriver");
                socketServer = loader.getExtension(serverDriver);
                socketServer.start(properties);
            } catch (Exception e) {
                logger.error("[ {} ] 启动服务失败! 参数为{}", properties.getProperty("name"), properties, e);
                System.exit(0);
            }
            countDownLatch.countDown();
            logger.info("{}-服务启动成功!", properties.getProperty("name"));
        }));
        NetUtil.isUsableLocalPort(0);
        try {
            countDownLatch.await();
            logger.info("全部服务端口启动完成！");
        } catch (InterruptedException e) {
            logger.error("中断异常", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 关闭服务端口
     *
     * @param port 关闭端口号
     */
    @Override
    public void close(Integer port) {
        socketServer.close(port);
    }

    /**
     * 关闭此服务所有端口
     */
    @Override
    public void closeAll() {
        logger.info("开始关闭此服务所有端口");
        threadPoolExecutor.shutdown();
        logger.info("socket server thread pool is shutting down!");
    }
}
