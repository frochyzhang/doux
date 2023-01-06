package com.allinfinance.dev.tools.hsp.service;

import com.allinfinance.dev.common.socket.client.ISocketService;
import com.allinfinance.dev.common.socket.client.dto.SocketRequestDTO;
import com.allinfinance.dev.common.socket.client.dto.SocketResponseDTO;
import com.allinfinance.dev.tools.hsp.config.CcpNetworkConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022-12-08 16:31
 */
@Service
public class ccpService {
    @Autowired
    private ISocketService socketService;

    @Value("${dev.hsp.tool.ip:10.250.32.231}")
    private String ip;

    @Value("${dev.hsp.tool.port1:11111}")
    private String port1;

    @Value("${dev.hsp.tool.port2:22222}")
    private String port2;

    @Value("${dev.hsp.tool.msg-length:6}")
    private String msgLength;

    @Autowired
    private CcpNetworkConfig networkConfig;

    public Boolean ccpTest(String msg1, String msg2) {
        SocketRequestDTO socketRequestDTO = new SocketRequestDTO(ip, port1, "ccp-test2");
        socketRequestDTO.setConnectionDriver("mina");
        SocketResponseDTO socketResponseDTO1 =
                socketService.clientRequest(socketRequestDTO, msg1);
        SocketRequestDTO socketRequestDTO1 = new SocketRequestDTO(ip, port2, "ccp-test1", msgLength, "UTF-8");
        socketRequestDTO1.setConnectionDriver("mina");
        SocketResponseDTO socketResponseDTO2 =
                socketService.clientRequest(socketRequestDTO1, msg2);

//        SocketResponseDTO socketResponseDTO1 =
//                socketService.clientRequest(new SocketRequestDTO("10.252.64.145", "11111","ccp-test2"), msg1);
//        SocketResponseDTO socketResponseDTO2 =
//                socketService.clientRequest(new SocketRequestDTO("10.252.64.145", "22222","ccp-test1","4","UTF-8"), msg2);

        if (socketResponseDTO1.getResponse().hashCode() == 248086510 && socketResponseDTO2.getResponse().hashCode() == 248086510){
            return true;
        }
        if (socketResponseDTO1.getResponse().hashCode() == 691847891 && socketResponseDTO2.getResponse().hashCode() == 776035358) {
            return true;
        } else {
            return false;
        }
    }
}
