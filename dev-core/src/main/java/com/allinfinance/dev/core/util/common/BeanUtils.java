package com.allinfinance.dev.core.util.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.allinfinance.dev.core.util.common.StrUtils.toLowerCaseFirstOne;

/**
 * BeanUtil
 * 对象属性工具类
 */
public class BeanUtils {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * 对象属性深度复制函数，用于将source对象中与target对象属性名
     * 相同的属性值赋值给target对象
     *
     * @param source 复制源对象
     * @param target 复制目标对象
     */
    public static void copyProperties(Object source, Object target) {
        Assert.notNull(source, "Source must not be null!");
        Assert.notNull(target, "Target must not be null!");

        Map<String, String> maps = new HashMap<>(16);
        try {
            getProperties(source, maps);

            Field[] targetFields = target.getClass().getDeclaredFields();
            for (Field targetField : targetFields) {
                String targetFieldName = targetField.getName();
                targetField.setAccessible(true);
                targetField.set(target, maps.get(targetFieldName));
            }
        } catch (IllegalAccessException ex) {
            logger.error("属性复制异常!");
            ex.printStackTrace();
        }
    }

    /**
     * 将全类名转换成以Impl结尾的bean名称
     *
     * @param className
     * @return beaName
     */
    public static String getBeanNameWithImpl(String className) {
        return StringUtils.join(getDefaultBeanName(className), "Impl");
    }

    /**
     * 将全类名转换成以类名首字母小写的bean名称
     *
     * @param className
     * @return beanName
     */
    public static String getDefaultBeanName(String className) {
        if (StringUtils.isNotBlank(className)) {
            String[] split = StringUtils.split(className, ".");
            return toLowerCaseFirstOne(split[split.length - 1]);
        } else {
            return "";
        }
    }

    private static void getProperties(Object source, Map<String, String> properties) throws IllegalAccessException {

        Field[] sourceFields = source.getClass().getDeclaredFields();
        for (Field sourceField : sourceFields) {
            String sourceFieldName = sourceField.getName();
            sourceField.setAccessible(true);
            Object sourceFieldValue = sourceField.get(source);
            if (sourceFieldValue == null) {
                continue;
            }
            if (sourceField.getType() == String.class) {
                properties.put(sourceFieldName, (String) sourceFieldValue);
            } else {
                getProperties(sourceFieldValue, properties);
            }
        }
    }
}
