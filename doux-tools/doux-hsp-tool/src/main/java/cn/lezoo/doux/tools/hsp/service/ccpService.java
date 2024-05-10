package cn.lezoo.doux.tools.hsp.service;

import cn.lezoo.doux.common.socket.api.client.SocketClientService;
import cn.lezoo.doux.common.socket.api.client.dto.SocketRequestDTO;
import cn.lezoo.doux.common.util.convert.PropertiesParseUtils;
import cn.lezoo.doux.framework.extension.loader.ExtensionLoader;
import cn.lezoo.doux.framework.extension.loader.ExtensionLoaderFactory;
import cn.lezoo.doux.framework.socket.client.driver.Connection;
import cn.lezoo.doux.tools.hsp.config.CcpNetworkConfig;
import cn.lezoo.doux.tools.hsp.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022-12-08 16:31
 */
@Service
public class ccpService {
    @Autowired
    private SocketClientService socketService;

    @Value("${doux.hsp.ip:127.0.0.1}")
    private String ip;

    @Value("${doux.hsp.port1:11111}")
    private String port1;

    @Value("${doux.hsp.port2:22222}")
    private String port2;

    @Value("${doux.hsp.test1-MsgLength:6}")
    private String msgLength1;

    @Value("${doux.hsp.test2-MsgLength:4}")
    private String msgLength2;

    @Value("${doux.tool.mina-flag:false}")
    private Boolean minaFlag;
    private Integer count = 1;
    @Autowired
    private CcpNetworkConfig networkConfig;
    public static final Logger logger = LoggerFactory.getLogger(ccpService.class);

    public Boolean ccpTest(String msg1, String msg2) {

//        SocketRequestDTO socketRequestDTO = new SocketRequestDTO(ip, port1, "ccp-test1", msgLength1, "UTF-8");
//        if (minaFlag) {
//            socketRequestDTO.setConnectionDriver("mina");
//        }
//        SocketResponseDTO socketResponseDTO1 =
//                socketService.clientRequest(socketRequestDTO, msg1);
//
//
//        SocketRequestDTO socketRequestDTO1 = new SocketRequestDTO(ip, port2, "ccp-test2", msgLength2, "UTF-8");
//        if (minaFlag) {
//            socketRequestDTO1.setConnectionDriver("mina");
//        }
//        SocketResponseDTO socketResponseDTO2 =
//                socketService.clientRequest(socketRequestDTO1, msg2);
//
//        String labelValue1 = StrUtils.getLabelValue(socketResponseDTO1.getResponse(), "<test>");
//        String labelValue2 = StrUtils.getLabelValue(socketResponseDTO2.getResponse(), "<test>");
//        if ("11111".equals(labelValue1) && "22222".equals(labelValue2)) {
//            return true;
//        }
        Properties properties = new Properties();
        SocketRequestDTO socketRequestDTO;
        if (count % 2 == 0) {
            socketRequestDTO = new SocketRequestDTO(ip, port1, "ccp", "6", "UTF-8");
        } else {
            socketRequestDTO = new SocketRequestDTO(ip, port2, "ccp", "4", "UTF-8");
        }
//        socketRequestDTO = new SocketRequestDTO(ip, port1, "ccp", "4", "UTF-8");
        PropertiesParseUtils.fromBean(properties, socketRequestDTO);
        String connectionDriver = properties.getProperty("connectionDriver");
        ExtensionLoader<Connection> loader = ExtensionLoaderFactory.getExtensionLoader(Connection.class);
        Connection connection = loader.getExtension(connectionDriver);
        connection.connect(properties);
        String time = StringUtils.getRandomString(10);
        logger.info("第次{}发送，发送消息为：{}", count++, time);
        String result = connection.send(time);
        if (time.equals(result)) {
            return true;
        }
        return false;
    }
}
