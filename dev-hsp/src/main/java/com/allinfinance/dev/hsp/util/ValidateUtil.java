package com.allinfinance.dev.hsp.util;

import cn.hutool.core.util.ReflectUtil;
import com.allinfinance.dev.core.util.validate.Check;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author huanghf
 * @date 2022/3/24 15:49
 */
public class ValidateUtil {
    private ValidateUtil() {
    }

    /**
     * 获取value对象的所有field，不包括父类的
     *
     * @param value 要获取field的对象
     * @return field数组
     */
    public static Field[] getAllFields(Object value) {
        Class<?> aClass = value.getClass();
        ArrayList<Field> list = new ArrayList<>();
        while (aClass != null) {
            list.addAll(new ArrayList<>(Arrays.asList(aClass.getDeclaredFields())));
            aClass = aClass.getSuperclass();
        }

        Field[] fields = new Field[list.size()];
        list.toArray(fields);
        return fields;
    }

    /**
     * 对value对象中添加了@Check注解的字段进行校验
     *
     * @param value 需要校验的对象
     */
    public static void validateValue(Object value) {
        Arrays.stream(getAllFields(value))
                .filter(field -> field.isAnnotationPresent(Check.class))
                .forEach(field -> {
                    Check check = field.getAnnotation(Check.class);
                    ReflectUtil.setAccessible(field);
                    Object v;
                    try {
                        v = field.get(value);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        throw new IllegalArgumentException(e);
                    }

                    /*
                    存在性验证开始
                     */
                    if (v == null) {
                        //校验存在性
                        throw new IllegalArgumentException(field + " 为空");
                    }
                    /*
                    存在性验证结束
                    长度校验开始
                     */
                    if (v instanceof String) {
                        int byteNum = ((String) v).getBytes().length;
                        if (check.maxLength() > 0 && byteNum > check.maxLength()) {
                            throw new IllegalArgumentException(field + " 长度大于最大字节数" + check.maxLength() + " 当前字节数为: " + byteNum);
                        }
                    }
                });
    }
}
