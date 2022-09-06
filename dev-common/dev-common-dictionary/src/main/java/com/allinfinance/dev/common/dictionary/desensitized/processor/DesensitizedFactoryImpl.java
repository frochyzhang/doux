package com.allinfinance.dev.common.dictionary.desensitized.processor;

import com.allinfinance.dev.common.dictionary.desensitized.DesensitizedType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2021/7/3 23:06
 */
@Component
public class DesensitizedFactoryImpl implements DesensitizedFactory {

    private static final Map<DesensitizedType, DesensitizedProcessor> PROCESSORS = new ConcurrentHashMap<>();

    /**
     * 注册脱敏处理器
     *
     * @param processor 脱敏处理器
     */
    @Override
    public void register(DesensitizedProcessor processor) {
        PROCESSORS.put(processor.supportType(), processor);
    }

    /**
     * 脱敏处理
     *
     * @param object 脱敏字符串
     * @param type   脱敏类型
     * @return 脱敏后字符串
     */
    @Override
    public String desensitized(String object, DesensitizedType type) {
        return PROCESSORS.get(type).desensitize(object);
    }

    public static Map<DesensitizedType, DesensitizedProcessor> getProcessors() {
        return PROCESSORS;
    }
}
