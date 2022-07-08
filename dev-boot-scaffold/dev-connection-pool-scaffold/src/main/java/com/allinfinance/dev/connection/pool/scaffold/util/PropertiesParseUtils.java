package com.allinfinance.dev.connection.pool.scaffold.util;

import com.allinfinance.dev.connection.pool.scaffold.configure.ServerMetadataConfigure;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Properties;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/8
 **/
public class PropertiesParseUtils {
    /**
     * 将ServerMetadataConfigure中配置的参数装配到properties中
     *
     * @param configure
     * @return
     */
    public static Properties fromServerMetadataConfigure(ServerMetadataConfigure configure) {
        Properties properties = new Properties();
        Field[] declaredFields = configure.getClass().getDeclaredFields();
        Arrays.stream(declaredFields).forEach(field -> {
            try {
                Object o = field.get(configure);
                if (o != null) {
                    properties.setProperty(field.getName(), (String) o);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return properties;
    }
}
