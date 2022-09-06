package com.allinfinance.dev.common.dictionary.desensitized.processor;


import com.allinfinance.dev.common.dictionary.desensitized.DesensitizedType;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2021/7/3 21:31
 */
public interface DesensitizedFactory {
    /**
     * 注册脱敏处理器
     *
     * @param processor 脱敏处理器
     */
    void register(DesensitizedProcessor processor);

    /**
     * 脱敏处理
     *
     * @param object 脱敏字符串
     * @param type   脱敏类型
     * @return 脱敏后字符串
     */
    String desensitized(String object, DesensitizedType type);
}
