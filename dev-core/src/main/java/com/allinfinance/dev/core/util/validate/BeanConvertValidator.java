package com.allinfinance.dev.core.util.validate;

import com.allinfinance.dev.core.constant.CommonConstants;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * @author 张勇
 * @description
 * @date 2020/12/7 23:24
 */
public class BeanConvertValidator {
    private static final Logger logger = LoggerFactory.getLogger(BeanConvertValidator.class);

    private static Boolean required = false;

    public static Boolean beanVerify(Object obj, String encoding) throws IllegalArgumentException {
        if (!required) {
            return Boolean.TRUE;
        }
        Field[] objFields = obj.getClass().getDeclaredFields();
        for (Field objField : objFields) {
            objField.setAccessible(true);
            String filedName = objField.getName();
            Object objFieldValue = null;
            try {
                objFieldValue = objField.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            Check checkAnno = objField.getAnnotation(Check.class);
            if (checkAnno != null) {
                if (objFieldValue == null) {
                    throw new IllegalArgumentException(filedName + " 必须不为null!");
                }
                if (checkAnno.type() == String.class) {
                    //通用字符串校验
                    String value = (String) objFieldValue;
                    if (StringUtils.isBlank(value)) {
                        throw new IllegalArgumentException(filedName + " 不允许为空值!");
                    }
                    int valueLength = 0;
                    try {
                        valueLength = value.getBytes(encoding).length;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    boolean maxMinVerify = checkAnno.length() != 0 && valueLength != checkAnno.length();
                    boolean minVerify = checkAnno.minLength() != 0 && valueLength < checkAnno.minLength();
                    boolean maxVerify = checkAnno.maxLength() != 0 && valueLength != checkAnno.maxLength();
                    if (maxMinVerify || minVerify || maxVerify) {
                        throw new IllegalArgumentException(filedName + " 长度不符合要求,源数据为: " + value);
                    }
                    if (StringUtils.isNotBlank(checkAnno.regex()) && Pattern.matches(checkAnno.regex(), value)) {
                        throw new IllegalArgumentException(filedName + " 不符合正则限定, 源数据为: " + value);
                    }
                } else if (!(objFieldValue instanceof Number)) {
                    // TODO: 2020/12/8 此处需注意调整具体字段类型，若存在非数值类型或者字符串类型时，需调整此处
                    throw new NumberFormatException(filedName + " 数值转换异常, 源数据为: " + objFieldValue);
                }
            }
        }
        return Boolean.TRUE;
    }

    // TODO: 2021/1/22 此处待优化
    static {
        Configurations configurations = new Configurations();

        String fileParentPath = BeanConvertValidator.class.getClassLoader().getResource(CommonConstants.FILE_PARENT_PATH).getPath();
        String[] configFiles = new File(fileParentPath).list((dir, name) -> {
            if (name.endsWith(CommonConstants.FILE_SUF_FIX)) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        });

        if (ArrayUtils.isNotEmpty(configFiles)) {
            for (String fileName : configFiles) {
                PropertiesConfiguration properties = null;
                try {
                    properties = configurations.properties(fileName);
                } catch (ConfigurationException e) {
                    e.printStackTrace();
                }
                String value = properties.getString(CommonConstants.XML_BEAN_VALIDATOR_REQUIRE);
                if (null != value) {
                    required = Boolean.valueOf(value);
                }
            }
        }

        logger.info("获取字段校验开关--dev.xml.field.verify:{}", BeanConvertValidator.required);
    }
}
