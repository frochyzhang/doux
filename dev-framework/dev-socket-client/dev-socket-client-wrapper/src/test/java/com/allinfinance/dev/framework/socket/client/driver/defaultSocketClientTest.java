package com.allinfinance.dev.framework.socket.client.driver;

import com.allinfinance.dev.framework.extension.loader.ExtensionLoader;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;
import org.junit.jupiter.api.Test;

import java.util.Properties;

class DefaultSocketClientTest {

    @Test
    void send() {
        Properties prop = new Properties();
        prop.put("connectionDriver", "socketNetty");
        prop.put("serverIp", "127.0.0.1");
        prop.put("serverPort", "4396");
        prop.put("msgLengthSize", "4");
        prop.put("msgEncode", "UTF-8");
        prop.put("clientAppName", "test-diy");
        prop.put("timeOutSeconds", "30");

        ExtensionLoader<SocketClient> clientExtensionLoader = ExtensionLoaderFactory.getExtensionLoader(SocketClient.class);
        SocketClient socketClient = clientExtensionLoader.getExtension("default");
        for (int i = 0; i < 10; i++) {
            String resp = socketClient.send(prop, i + "alskdjfalkdsjflkasjdlka" + System.currentTimeMillis());
            System.out.println(resp);
        }
    }
}