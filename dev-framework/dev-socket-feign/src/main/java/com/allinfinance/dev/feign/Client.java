package com.allinfinance.dev.feign;

import com.allinfinance.dev.common.socket.api.client.dto.SocketRequestDTO;
import com.allinfinance.dev.common.util.convert.PropertiesParseUtils;
import com.allinfinance.dev.common.util.xml.xstream.XStreamUtils;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;
import com.allinfinance.dev.framework.socket.client.driver.SocketClient;

import java.util.Properties;

public interface Client {
    <T> T execute(SocketRequestDTO connectParams, Object data, Class<T> returnType) throws Exception;

    class Default implements Client {
        @Override
        public <T> T execute(SocketRequestDTO connectParams, Object data, Class<T> returnType) throws Exception {
            Properties properties = new Properties();
            PropertiesParseUtils.fromBean(properties, connectParams);
            String xml = XStreamUtils.beanToXml(data);
            String result = ExtensionLoaderFactory.getExtension(SocketClient.class, "default")
                .send(properties, xml);
            return XStreamUtils.xmlToBean(result, returnType);
        }
    }
}
