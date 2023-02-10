package com.allinfinance.dev.socket.server.scaffold.service;

import com.allinfinance.dev.framework.extension.loader.ExtensionLoader;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;
import com.allinfinance.dev.framework.socket.server.driver.SocketServerWrapper;
import com.allinfinance.dev.socket.server.scaffold.api.ISocketServerService;
import com.allinfinance.dev.socket.server.scaffold.configure.ServerBootstrapConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022-09-16 15:16
 */
@Component
@ConditionalOnProperty(prefix = "com.allinfinance.socket.server.bootstrap", name = "serverEnabled", havingValue = "true")
public class ISocketServerServiceImpl implements ISocketServerService, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(ISocketServerServiceImpl.class);

    @Autowired
    @Qualifier("socketServerList")
    private List<Properties> socketServerList;

    @Autowired
    private ServerBootstrapConfigure configure;

    private SocketServerWrapper socketServer;

    /**
     * 根据传入的socketbeans开启多端口监听
     */
    @Override
    public void start() {
        if (logger.isDebugEnabled()) {
            logger.debug("启动socketServer多端口服务：");
        }
        socketServer.start(socketServerList);
    }

    @Override
    public void close(Integer port) {
        socketServer.close(port);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ExtensionLoader<SocketServerWrapper> extensionLoader = ExtensionLoaderFactory.getExtensionLoader(SocketServerWrapper.class);
        socketServer = extensionLoader.getExtension(configure.getBootstrap());
        start();
    }

    @Override
    public void destroy() throws Exception {
        socketServer.closeAll();
    }
}
