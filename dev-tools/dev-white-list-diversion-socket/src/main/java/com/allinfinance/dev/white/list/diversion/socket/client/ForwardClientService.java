package com.allinfinance.dev.white.list.diversion.socket.client;

import com.allinfinance.dev.common.socket.api.client.dto.SocketRequestDTO;
import com.allinfinance.dev.common.util.convert.PropertiesParseUtils;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;
import com.allinfinance.dev.framework.socket.client.driver.SocketClient;
import com.allinfinance.dev.white.list.diversion.socket.config.WhiteListConfig;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * @author huanghf
 * @date 2024/3/18 22:06
 */
@Service
public class ForwardClientService {
    private final WhiteListConfig whiteListConfig;

    public ForwardClientService(WhiteListConfig whiteListConfig) {
        this.whiteListConfig = whiteListConfig;
    }

    public String forward(String reqMsg) {
        SocketRequestDTO socketRequestDTO = new SocketRequestDTO(whiteListConfig.getIp(), whiteListConfig.getPort(), whiteListConfig.getMsgLengthSize(), whiteListConfig.getMsgEncode());
        socketRequestDTO.setTimeout(String.valueOf(whiteListConfig.getTimeoutSec() * 1000));
        SocketClient socketClient = ExtensionLoaderFactory.getExtension(SocketClient.class, socketRequestDTO.getSocketClient());
        Properties properties = new Properties();
        PropertiesParseUtils.fromBean(properties, socketRequestDTO);
        return socketClient.send(properties, reqMsg);
    }
}
