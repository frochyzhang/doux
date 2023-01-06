package com.allinfinance.dev.ccp.service;

import com.allinfinance.dev.common.socket.client.ISocketService;
import com.allinfinance.dev.common.socket.client.dto.SocketRequestDTO;
import com.allinfinance.dev.common.socket.client.dto.SocketResponseDTO;
import com.allinfinance.dev.common.util.convert.PropertiesParseUtils;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;
import com.allinfinance.dev.framework.socket.client.driver.SocketClient;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/06 16:24
 */
@Service("socketService")
public class SocketServiceImpl implements ISocketService {

    private static final Logger logger = LoggerFactory.getLogger(SocketServiceImpl.class);

    @Value("${dev.ccp.flag:false}")
    private Boolean flag;
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

        if (flag){
            socketResponseDTO.setSuccess(true);
            socketResponseDTO.setResponse("\"<SERVICE xmlns=\\\"http://www.allinfinance.com/dataspec/\\\" xmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\">\\n\" +\n" +
                    "                \"  <SERVICE_HEADER>\\n\" +\n" +
                    "                \"    <SERVICE_SN>2022101910152998160</SERVICE_SN>\\n\" +\n" +
                    "                \"    <SERVICE_ID>11010</SERVICE_ID>\\n\" +\n" +
                    "                \"    <ORG>000066666666</ORG>\\n\" +\n" +
                    "                \"    <CHANNEL_ID>01</CHANNEL_ID>\\n\" +\n" +
                    "                \"    <OP_ID />\\n\" +\n" +
                    "                \"    <REQUST_TIME>20221019101529</REQUST_TIME>\\n\" +\n" +
                    "                \"    <VERSION_ID>01</VERSION_ID>\\n\" +\n" +
                    "                \"    <SERV_RESPONSE>\\n\" +\n" +
                    "                \"      <STATUS>S</STATUS>\\n\" +\n" +
                    "                \"      <CODE>SSSS</CODE>\\n\" +\n" +
                    "                \"      <DESC>交易成功</DESC>\\n\" +\n" +
                    "                \"    </SERV_RESPONSE>\\n\" +\n" +
                    "                \"    <RES_SERVICE_SN>2022101910152998160</RES_SERVICE_SN>\\n\" +\n" +
                    "                \"    <RES_SERVICE_TIME>20221019102500</RES_SERVICE_TIME>\\n\" +\n" +
                    "                \"  </SERVICE_HEADER>\\n\" +\n" +
                    "                \"  <SERVICE_BODY>\\n\" +\n" +
                    "                \"    <RESPONSE>\\n\" +\n" +
                    "                \"      <ID_NO>150101200105240088</ID_NO>\\n\" +\n" +
                    "                \"      <ID_TYPE>I</ID_TYPE>\\n\" +\n" +
                    "                \"      <TITLE/>\\n\" +\n" +
                    "                \"      <NAME>睡的好</NAME>\\n\" +\n" +
                    "                \"      <GENDER>F</GENDER>\\n\" +\n" +
                    "                \"      <BIRTHDAY>20010524</BIRTHDAY>\\n\" +\n" +
                    "                \"      <OCCUPATION>H</OCCUPATION>\\n\" +\n" +
                    "                \"      <OCCUPATION_SUB_TYPE/>\\n\" +\n" +
                    "                \"      <OCCUPATION_EXTEND/>\\n\" +\n" +
                    "                \"      <BANKMEMBER_NO/>\\n\" +\n" +
                    "                \"      <NATIONALITY>156</NATIONALITY>\\n\" +\n" +
                    "                \"      <MARITAL_STATUS>O</MARITAL_STATUS>\\n\" +\n" +
                    "                \"      <QUALIFICATION/>\\n\" +\n" +
                    "                \"      <HOME_PHONE/>\\n\" +\n" +
                    "                \"      <MOBILE_NO>15956888888</MOBILE_NO>\\n\" +\n" +
                    "                \"      <EMAIL>zzxvva@qq.com</EMAIL>\\n\" +\n" +
                    "                \"      <SETUP_DATE>20370622</SETUP_DATE>\\n\" +\n" +
                    "                \"      <CORP_NAME>未知</CORP_NAME>\\n\" +\n" +
                    "                \"      <DEPARTMENT/>\\n\" +\n" +
                    "                \"      <EMB_NAME>SHUI DEHAO</EMB_NAME>\\n\" +\n" +
                    "                \"      <BANK_CUSTOMER_ID/>\\n\" +\n" +
                    "                \"      <ID_START_DATE/>\\n\" +\n" +
                    "                \"      <ID_EXPIRE_DATE/>\\n\" +\n" +
                    "                \"      <ID_ISSUER_ADDR>内蒙古呼和浩特市市辖区</ID_ISSUER_ADDR>\\n\" +\n" +
                    "                \"      <CREATED_TIME>20210524155603</CREATED_TIME>\\n\" +\n" +
                    "                \"      <CUST_CAT_CD/>\\n\" +\n" +
                    "                \"      <COMP_POST>Z</COMP_POST>\\n\" +\n" +
                    "                \"      <COMP_STRUCTURE>Z</COMP_STRUCTURE>\\n\" +\n" +
                    "                \"      <COMP_TYPE>Z</COMP_TYPE>\\n\" +\n" +
                    "                \"      <COMP_TEL/>\\n\" +\n" +
                    "                \"      <COMP_FAX/>\\n\" +\n" +
                    "                \"      <EMP_STATUS>Y</EMP_STATUS>\\n\" +\n" +
                    "                \"      <CORP_BEGDATE/>\\n\" +\n" +
                    "                \"      <EMP_STABILITY>B</EMP_STABILITY>\\n\" +\n" +
                    "                \"      <SOCIAL_INS_AMT>0.00</SOCIAL_INS_AMT>\\n\" +\n" +
                    "                \"      <CORP_REVENUE_PER_YEAR>0.00</CORP_REVENUE_PER_YEAR>\\n\" +\n" +
                    "                \"      <LANGUAGE_IND/>\\n\" +\n" +
                    "                \"      <PR_OF_COUNTRY/>\\n\" +\n" +
                    "                \"      <RESIDENCY_COUNTRY_CD>156</RESIDENCY_COUNTRY_CD>\\n\" +\n" +
                    "                \"      <HOME_STAND_FROM/>\\n\" +\n" +
                    "                \"      <LIQUID_ASSET/>\\n\" +\n" +
                    "                \"      <HOUSE_TYPE/>\\n\" +\n" +
                    "                \"      <HOUSE_OWNERSHIP/>\\n\" +\n" +
                    "                \"      <CUST_ID>1145</CUST_ID>\\n\" +\n" +
                    "                \"      <IS_SET_CUSTOMER_PWD/>\\n\" +\n" +
                    "                \"      <CONTACT_FLAG>Y</CONTACT_FLAG>\\n\" +\n" +
                    "                \"      <ID_ISSUER/>\\n\" +\n" +
                    "                \"      <MKT_MSG_ALLOW/>\\n\" +\n" +
                    "                \"      <JOB_INFO/>\\n\" +\n" +
                    "                \"      <NATION/>\\n\" +\n" +
                    "                \"      <SOURCE_OF_INCOME/>\\n\" +\n" +
                    "                \"      <CASH_OVERSEA_BLACK_IND>N</CASH_OVERSEA_BLACK_IND>\\n\" +\n" +
                    "                \"    </RESPONSE>\\n\" +\n" +
                    "                \"  </SERVICE_BODY>\\n\" +\n" +
                    "                \"</SERVICE1111>\\n\"");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("返回消息为{}", socketResponseDTO);
            return socketResponseDTO;
        }

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
