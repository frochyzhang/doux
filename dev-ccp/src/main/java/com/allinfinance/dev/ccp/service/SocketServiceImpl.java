package com.allinfinance.dev.ccp.service;

import com.allinfinance.dev.common.socket.client.ISocketService;
import com.allinfinance.dev.common.socket.client.dto.SocketRequestDTO;
import com.allinfinance.dev.common.socket.client.dto.SocketResponseDTO;
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

    private static final Logger logger = LoggerFactory.getLogger(SocketServiceImpl.class);

    /**
     * 客户端请求
     *
     * @param socketRequestDTO 请求连接参数
     * @param message          请求内容
     * @return 请求响应
     */
    @Override
    public SocketResponseDTO clientRequest(SocketRequestDTO socketRequestDTO, String message) {
        logger.info("请求连接参数{}", socketRequestDTO);
        logger.info("请求内容{}", message);
        SocketResponseDTO socketResponseDTO = new SocketResponseDTO();
        Properties properties = new Properties();
        PropertiesParseUtils.fromBean(properties, socketRequestDTO);
        SocketClient socketClient = ExtensionLoaderFactory.getExtensionLoader(SocketClient.class)
                .getExtension(socketRequestDTO.getSocketClient());
        String resp = socketClient.send(properties, message);
        if (StringUtils.isBlank(resp)) {
            logger.error("返回消息为空");
            socketResponseDTO.setSuccess(false);
            return socketResponseDTO;
        }
        socketResponseDTO.setSuccess(true);
        socketResponseDTO.setResponse(resp);
        logger.info("返回消息为{}", socketResponseDTO);
        return socketResponseDTO;
    }
}
