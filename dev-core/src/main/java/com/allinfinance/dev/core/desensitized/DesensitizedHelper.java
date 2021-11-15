package com.allinfinance.dev.core.desensitized;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2021/7/3 02:14
 */
public interface DesensitizedHelper {
    /**
     * 脱敏对象
     *
     * @param object 待脱敏对象
     * @return 脱敏后对象
     */
    String toString(Object object);

    /**
     * 单字段脱敏
     *
     * @param object   待脱敏字段
     * @param typeEnum 脱敏类型
     * @return 脱敏后字段
     */
    String toString(String object, DesensitizedType typeEnum);
}
