//package com.allinfinance.dev.core.loader;
//
//import com.allinfinance.dev.core.bean.AbstractConfigBean;
//import com.allinfinance.dev.core.constant.SocketBeanLoaderEnum;
//import org.apache.commons.configuration2.PropertiesConfiguration;
//import org.apache.commons.configuration2.builder.fluent.Configurations;
//import org.apache.commons.configuration2.ex.ConfigurationException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author 张勇
// * @description
// * @date 2020/12/1 20:49
// */
//public class BeanLoader<T extends AbstractConfigBean> {
//
//    private static final Logger logger = LoggerFactory.getLogger(BeanLoader.class);
//
//    private final String propertyFileParentPath;
//    private final String propertyFilePrefix;
//    private final String propertyFileEndfix;
//
//    public BeanLoader(Class<?> baseBeanClass, String propertyFileParentPath, String propertyFilePrefix, String propertyFileEndfix) {
//        this.propertyFileParentPath = propertyFileParentPath;
//        this.propertyFilePrefix = propertyFilePrefix;
//        this.propertyFileEndfix = propertyFileEndfix;
//    }
//
//    private List<T> loadSocketBeansFromFile() throws Exception {
//        Configurations configurations = new Configurations();
//        String fileParentPath = this.getClass().getResource(getPropertyFileParentPath()).getPath();
//        logger.info("加载配置文件路径:{}", fileParentPath);
//
//        //文件名必须以【socket-】开头，以【.properties】结束
//        String[] configFiles = new File(fileParentPath).list((dir, name) -> {
//            if (name.startsWith(propertyFilePrefix) && name.endsWith(propertyFileEndfix)) {
//                return Boolean.TRUE;
//            }
//            return Boolean.FALSE;
//        });
//
//        //若未指定配置文件，则不返回默认配置信息，抛出异常，结束程序
//        if (configFiles == null || configFiles.length == 0) {
//            logger.error("未指定socket配置文件，服务启动失败!");
//            throw new Exception("未指定socket配置文件");
//        }
//
//        //遍历文件名读取配置信息
//        List<T> socketBeans = new ArrayList<>();
//        try {
//            for (String fileName : configFiles) {
//                PropertiesConfiguration properties = configurations.properties(fileName);
//                Map<SocketBeanLoaderEnum, String> propertyMap = SocketBeanLoaderEnum.getPropertyValue(properties);
//                T minaSocketBean = new T(propertyMap);
//                socketBeans.add(minaSocketBean);
//            }
//        } catch (ConfigurationException ex) {
//            logger.error("配置文件读取失败!", ex);
//        }
//        return socketBeans;
//    }
//
//    public String getPropertyFileParentPath() {
//        return propertyFileParentPath;
//    }
//
//    public String getPropertyFilePrefix() {
//        return propertyFilePrefix;
//    }
//
//    public static Logger getLogger() {
//        return logger;
//    }
//}
