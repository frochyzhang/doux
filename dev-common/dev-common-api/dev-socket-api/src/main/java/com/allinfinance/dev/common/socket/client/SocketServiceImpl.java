package com.allinfinance.dev.common.socket.client;

import com.allinfinance.dev.common.socket.client.pojo.SocketRequestDTO;
import com.allinfinance.dev.common.socket.client.pojo.SocketResponseDTO;
import com.allinfinance.dev.common.util.convert.PropertiesParseUtils;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;
import com.allinfinance.dev.framework.socket.client.driver.SocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/06 16:24
 */
//@Service("socketService")
public class SocketServiceImpl implements ISocketService {
    private Logger logger = LoggerFactory.getLogger(SocketServiceImpl.class);

    @Override
    public SocketResponseDTO clientRequest(SocketRequestDTO socketRequestDTO,String message) {
        Properties properties = new Properties();
        PropertiesParseUtils.fromBean(properties, socketRequestDTO);

        SocketClient socketClient = ExtensionLoaderFactory.getExtensionLoader(SocketClient.class)
                .getExtension(socketRequestDTO.getSocketClient());

        String resp = socketClient.send(properties, message);

        return null;
    }

//    public static void main(String[] args) {
//        try {
//            SocketServiceImpl client = new SocketServiceImpl();
//            String reqMess = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                    "<SMS>\n" +
//                    "    <ORG_ID>000064163056</ORG_ID>\n" +
//                    "    <SMS_NO>113133000326</SMS_NO>\n" +
//                    "    <SMS_TYPE>00</SMS_TYPE>\n" +
//                    "    <TEL>13585961521</TEL>\n" +
//                    "    <CONTENT>您好,您用于开通快捷支付的验证码为[226230].</CONTENT>\n" +
//                    "    <REQUEST_TIME>20170612113133</REQUEST_TIME>\n" +
//                    "    <RESERVED></RESERVED>\n" +
//                    "</SMS>";
//            SocketRequestDTO socketRequestDTO = new SocketRequestDTO("127.0.0.1", 4396, "sms", 30, false, reqMess, 4, "UTF-8", false);
//            SocketResponseDTO response1 = client.clientRequest(socketRequestDTO);
//            System.out.println("resultResp:" + response1);
//        } finally {
//            System.out.println("end");
//        }
//    }
}
