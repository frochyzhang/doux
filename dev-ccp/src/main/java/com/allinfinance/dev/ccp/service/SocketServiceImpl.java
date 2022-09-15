package com.allinfinance.dev.ccp.service;

import com.allinfinance.dev.common.socket.client.ISocketService;
import com.allinfinance.dev.common.socket.client.pojo.SocketRequestDTO;
import com.allinfinance.dev.common.socket.client.pojo.SocketResponseDTO;
import com.allinfinance.dev.common.util.convert.PropertiesParseUtils;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;
import com.allinfinance.dev.framework.socket.client.driver.SocketClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/06 16:24
 */
@Service("socketService")
public class SocketServiceImpl implements ISocketService {
    private final Logger logger = LoggerFactory.getLogger(com.allinfinance.dev.core.util.socket.client.SocketServiceImpl.class);

    @Override
    public SocketResponseDTO clientRequest(SocketRequestDTO socketRequestDTO,String message) {
        SocketResponseDTO socketResponseDTO = new SocketResponseDTO();
        Properties properties = new Properties();
        PropertiesParseUtils.fromBean(properties, socketRequestDTO);

        SocketClient socketClient = ExtensionLoaderFactory.getExtensionLoader(SocketClient.class)
                .getExtension(socketRequestDTO.getSocketClient());

        String resp = socketClient.send(properties, message);
        if (resp == null || StringUtils.isEmpty(resp)){
            logger.debug("返回消息为空");
            socketResponseDTO.setSuccess(false);
            return socketResponseDTO;
        }
        socketResponseDTO.setSuccess(true);
        socketResponseDTO.setResponse(resp);
        return socketResponseDTO;
    }
}
