package com.allinfinance.dev.common.util.convert;

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
     * 将Object对象中中配置的参数装配到properties中
     *
     * @param object
     * @return
     */
    public static void fromBean(Properties properties, Object object) {
        if (properties == null) {
            properties = new Properties();
        }
        Field[] declaredFields = object.getClass().getDeclaredFields();
        Properties finalProperties = properties;
        Arrays.stream(declaredFields).forEach(field -> {
            try {
                field.setAccessible(true);
                Object o = field.get(object);
                if (o != null) {
                    finalProperties.setProperty(field.getName(), (String) o);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
