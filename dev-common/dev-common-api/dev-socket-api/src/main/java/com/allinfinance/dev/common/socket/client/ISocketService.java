package com.allinfinance.dev.common.socket.client;

import com.allinfinance.dev.common.socket.client.dto.SocketRequestDTO;
import com.allinfinance.dev.common.socket.client.dto.SocketResponseDTO;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/06 9:30
 */
public interface ISocketService {

    /**
     * 客户端请求
     *
     * @param socketRequestDTO 请求连接参数
     * @param message          请求内容
     * @return 请求响应
     */
    SocketResponseDTO clientRequest(SocketRequestDTO socketRequestDTO, String message);
}
