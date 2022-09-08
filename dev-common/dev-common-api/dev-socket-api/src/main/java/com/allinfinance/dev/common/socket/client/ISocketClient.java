package com.allinfinance.dev.common.socket.client;

import com.allinfinance.dev.common.socket.client.pojo.SocketRequestDTO;
import com.allinfinance.dev.common.socket.client.pojo.SocketResponseDTO;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/06 9:30
 */
public interface ISocketClient {
    /**
     *
     * @param socketRequestDTO
     * @return SocketResponseDTO
     */
    SocketResponseDTO clientRequest(SocketRequestDTO socketRequestDTO);
}
