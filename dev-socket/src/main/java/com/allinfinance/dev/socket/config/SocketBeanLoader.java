package com.allinfinance.dev.socket.config;

import com.allinfinance.dev.core.bean.MinaSocketBean;
import com.allinfinance.dev.core.constant.CommonConstants;
import com.allinfinance.dev.core.constant.SocketBeanLoaderEnum;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 张勇
 * @date 2020-11-28 01:24
 */
@Configuration
public class SocketBeanLoader {

    private static final Logger logger = LoggerFactory.getLogger(SocketBeanLoader.class);

    @Value("${dev.socket.config-path}")
    private String fileParentPath;

    @Bean(name = "socketBeans")
    public List<MinaSocketBean> loadSocketBeansFromFile() throws Exception {
        Configurations configurations = new Configurations();
//        String fileParentPath = System.getProperty(CommonConstants.FILE_PARENT_PATH);
        logger.info("加载配置文件路径:{}", fileParentPath);

        //文件名必须以【socket-】开头，以【.properties】结束
        String[] configFiles = new File(fileParentPath).list((dir, name) ->
                name.startsWith(CommonConstants.FILE_PREFIX) && name.endsWith(CommonConstants.FILE_SUF_FIX)
                        ? Boolean.TRUE
                        : Boolean.FALSE);

        //若未指定配置文件，则不返回默认配置信息，抛出异常，结束程序
        if (configFiles == null || configFiles.length == 0) {
            logger.error("未指定socket配置文件，服务启动失败!");
            throw new Exception("未指定socket配置文件");
        }

        //遍历文件名读取配置信息
        List<MinaSocketBean> socketBeans = new ArrayList<>();
        try {
            for (String fileName : configFiles) {
                PropertiesConfiguration properties = configurations.properties(fileParentPath + fileName);
                Map<SocketBeanLoaderEnum, String> propertyMap = SocketBeanLoaderEnum.getPropertyValue(properties);
                MinaSocketBean minaSocketBean = new MinaSocketBean(propertyMap);
                socketBeans.add(minaSocketBean);
            }
        } catch (Exception ex) {
            logger.error("配置文件读取失败!", ex);
            System.exit(0);
        }
        return socketBeans;
    }
}
