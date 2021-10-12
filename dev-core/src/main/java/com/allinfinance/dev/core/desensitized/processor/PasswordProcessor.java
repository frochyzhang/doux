package com.allinfinance.dev.core.desensitized.processor;

import com.allinfinance.dev.core.desensitized.DesensitizedType;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2021/7/4 00:39
 */
@Component
public class PasswordProcessor extends BaseProcessor {

    /**
     * 脱敏类型
     *
     * @return 脱敏类型
     */
    @Override
    public DesensitizedType supportType() {
        return DesensitizedType.PASSWORD;
    }

    /**
     * 脱敏方法
     *
     * @param object 待脱敏字符串
     * @return 脱敏后字符串
     */
    @Override
    public String desensitize(String object) {
        return "******";
    }
}
