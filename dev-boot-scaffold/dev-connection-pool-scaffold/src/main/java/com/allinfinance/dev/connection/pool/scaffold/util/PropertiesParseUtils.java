package com.allinfinance.dev.connection.pool.scaffold.util;

import com.allinfinance.dev.connection.pool.scaffold.configure.ConnectionPoolConfigure;
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
    public static Properties fromServerMetadataConfigure(Properties properties, ServerMetadataConfigure configure) {
        if (properties == null) {
            properties = new Properties();
        }
        Field[] declaredFields = configure.getClass().getDeclaredFields();
        Properties finalProperties = properties;
        Arrays.stream(declaredFields).forEach(field -> {
            try {
                field.setAccessible(true);
                Object o = field.get(configure);
                if (o != null) {
                    finalProperties.setProperty(field.getName(), (String) o);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return properties;
    }

    /**
     * 将ConnectionPoolConfigure中配置的参数装配到properties中
     *
     * @param configure
     * @return
     */
    public static Properties fromConnectionPoolConfigure(Properties properties, ConnectionPoolConfigure configure) {
        if (properties == null) {
            properties = new Properties();
        }

        Field[] declaredFields = configure.getClass().getSuperclass().getDeclaredFields();
        Properties finalProperties = properties;
        Arrays.stream(declaredFields).forEach(field -> {
            try {
                field.setAccessible(true);
                Object o = field.get(configure);
                if (o != null) {
                    finalProperties.setProperty(field.getName(), (String) o);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return properties;
    }
}
