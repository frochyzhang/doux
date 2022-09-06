package com.allinfinance.dev.common.dictionary.desensitized.processor;

import com.allinfinance.dev.common.dictionary.desensitized.DesensitizedType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2021/7/4 00:35
 */
@Component
public class MobileNoProcessor extends BaseProcessor {
    public MobileNoProcessor() {
    }

    /**
     * 脱敏类型
     *
     * @return 脱敏类型
     */
    @Override
    public DesensitizedType supportType() {
        return DesensitizedType.MOBILE_NO;
    }

    /**
     * 脱敏方法
     *
     * @param object 待脱敏字符串
     * @return 脱敏后字符串
     */
    @Override
    public String desensitize(String object) {
        return StringUtils.overlay(object, "****", 3, 7);
    }
}

