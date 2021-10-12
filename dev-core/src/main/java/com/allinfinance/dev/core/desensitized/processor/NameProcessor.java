package com.allinfinance.dev.core.desensitized.processor;

import com.allinfinance.dev.core.desensitized.DesensitizedType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2021/7/4 00:37
 */
@Component
public class NameProcessor extends BaseProcessor {
    public NameProcessor() {
    }

    /**
     * 脱敏类型
     *
     * @return 脱敏类型
     */
    @Override
    public DesensitizedType supportType() {
        return DesensitizedType.NAME;
    }

    /**
     * 脱敏方法
     *
     * @param object 待脱敏字符串
     * @return 脱敏后字符串
     */
    @Override
    public String desensitize(String object) {
        return StringUtils.isBlank(object) ? null : object.substring(0, 1) + this.suffix(object);
    }

    private String suffix(String object) {
        int len = object.length();
        StringBuilder stringBuilder = new StringBuilder("*");

        for (int i = 2; i < len; ++i) {
            stringBuilder.append("*");
        }

        return stringBuilder.toString();
    }
}
