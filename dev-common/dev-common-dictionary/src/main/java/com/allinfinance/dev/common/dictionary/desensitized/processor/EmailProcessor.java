package com.allinfinance.dev.common.dictionary.desensitized.processor;

import com.allinfinance.dev.common.dictionary.desensitized.DesensitizedType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2021/7/4 00:32
 */
@Component
public class EmailProcessor extends BaseProcessor {

    private String suffix(int len) {
        StringBuilder stringBuilder = new StringBuilder("*");

        for (int i = 1; i < len; ++i) {
            stringBuilder.append("*");
        }

        return stringBuilder.toString();
    }

    /**
     * 脱敏类型
     *
     * @return 脱敏类型
     */
    @Override
    public DesensitizedType supportType() {
        return DesensitizedType.EMAIL;
    }

    /**
     * 脱敏方法
     *
     * @param object 待脱敏字符串
     * @return 脱敏后字符串
     */
    @Override
    public String desensitize(String object) {
        if (StringUtils.isBlank(object)) {
            return null;
        } else if (!object.contains("@")) {
            return object;
        } else {
            String pre = object.split("@")[0];
            int len = pre.length();
            return len < 4 ? pre.charAt(0) + this.suffix(len - 1) + object.substring(len) : pre.substring(0, 3) + this.suffix(len - 3) + object.substring(len);
        }
    }
}