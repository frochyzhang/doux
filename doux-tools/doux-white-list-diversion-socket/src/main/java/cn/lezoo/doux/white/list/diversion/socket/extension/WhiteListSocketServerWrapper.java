package cn.lezoo.doux.white.list.diversion.socket.extension;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.lezoo.doux.framework.extension.annotation.Extension;
import cn.lezoo.doux.framework.extension.loader.ExtensionLoader;
import cn.lezoo.doux.framework.extension.loader.ExtensionLoaderFactory;
import cn.lezoo.doux.framework.socket.server.driver.SocketServer;
import cn.lezoo.doux.framework.socket.server.driver.SocketServerWrapper;
import cn.lezoo.doux.white.list.diversion.socket.filter.MinaWhiteListFilter;
import cn.lezoo.doux.white.list.diversion.socket.filter.NettyWhiteListFilter;
import org.apache.commons.lang3.StringUtils;
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
 * @author huanghf
 * @date 2024/3/18 17:28
 */
@Extension("white-list")
public class WhiteListSocketServerWrapper implements SocketServerWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhiteListSocketServerWrapper.class);

    private static ThreadPoolExecutor threadPoolExecutor;
    private SocketServer socketServer;

    @Override
    public void start(List<Properties> propertyList) {
        ExtensionLoader<SocketServer> loader = ExtensionLoaderFactory.getExtensionLoader(SocketServer.class);
        CountDownLatch countDownLatch = new CountDownLatch(propertyList.size());
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNamePrefix("socket-server-pool-")
                .setDaemon(true)
                .build();
        threadPoolExecutor = new ThreadPoolExecutor(propertyList.size(), propertyList.size(), 0L,
                TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(1), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
        LOGGER.info("正在启动应用，请稍后!");
        propertyList.forEach(properties -> threadPoolExecutor.submit(() -> {
            try {
                String serverDriver = properties.getProperty("serverDriver");
                String handlerClassName = properties.getProperty("handlerClassName");
                String filterClassName = properties.getProperty("filterClassName");
                switch (serverDriver) {
                    case "netty":
                        if (StringUtils.isNotBlank(handlerClassName)) {
                            properties.setProperty("handlerClassName", NettyWhiteListFilter.class.getName() + "," + handlerClassName);
                        } else {
                            throw new RuntimeException("Netty handler class name is empty!");
                        }
                        break;
                    case "mina":
                        if (StringUtils.isNotBlank(filterClassName)) {
                            properties.setProperty("filterClassName", MinaWhiteListFilter.class.getName() + "," + filterClassName);
                        } else {
                            properties.setProperty("filterClassName", MinaWhiteListFilter.class.getName());
                        }
                        break;
                    default:
                }
                // FIXME: 2024/3/25 一个netty，一个mina，怎么关闭端口
                socketServer = loader.getExtension(serverDriver);
                socketServer.start(properties);
            } catch (Exception e) {
                LOGGER.error("[ {} ] 服务启动失败! 参数为{}", properties.getProperty("name"), properties, e);
                System.exit(0);
            }
            countDownLatch.countDown();
            LOGGER.info("[ {} ] 服务启动成功!", properties.getProperty("name"));
        }));
        try {
            countDownLatch.await();
            LOGGER.info("全部服务端口启动完成！");
        } catch (InterruptedException e) {
            LOGGER.error("中断异常", e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void close(Integer port) {
        socketServer.close(port);
    }

    @Override
    public void closeAll() {
        LOGGER.info("开始关闭此服务所有端口");
        threadPoolExecutor.shutdown();
        LOGGER.info("socket server thread pool is shutting down!");
    }
}
