package com.allinfinance.dev.socket.server.scaffold.service;

import com.allinfinance.dev.framework.extension.loader.ExtensionLoader;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;
import com.allinfinance.dev.framework.socket.server.driver.SocketServerWrapper;
import com.allinfinance.dev.socket.server.scaffold.api.ISocketServerService;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022-09-16 15:16
 */
@Component
public class ISocketServerServiceImpl implements ISocketServerService {

    private Logger logger = LoggerFactory.getLogger(ISocketServerServiceImpl.class);

    @Autowired
    private List<Properties> socketServerList;

    /**
     * 根据传入的socketbeans开启多端口监听
     */
    @Override
    public void start() {
        ExtensionLoader<SocketServerWrapper> extensionLoader = ExtensionLoaderFactory.getExtensionLoader(SocketServerWrapper.class);
        SocketServerWrapper socketServer = extensionLoader.getExtension(socketServerList.get(0).getProperty("socketServer"));
        socketServer.start(socketServerList);
    }
}
