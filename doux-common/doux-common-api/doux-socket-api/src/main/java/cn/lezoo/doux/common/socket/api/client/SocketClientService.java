package cn.lezoo.doux.common.socket.api.client;

import cn.lezoo.doux.common.socket.api.client.dto.SocketRequestDTO;
import cn.lezoo.doux.common.socket.api.client.dto.SocketResponseDTO;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/06 9:30
 */
public interface SocketClientService {

    /**
     * 客户端请求
     *
     * @param socketRequestDTO 请求连接参数
     * @param message          请求内容
     * @return 请求响应
     */
    SocketResponseDTO request(SocketRequestDTO socketRequestDTO, String message);
}
