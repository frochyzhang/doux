package com.allinfinance.dev.rpc.scaffold.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * @author qipeng
 * @date 2021/12/30 16:44
 */

public class BeanNameUtils {

    public static String getBeanName(String className) {
        if (StringUtils.isNotBlank(className)) {
            String[] split = className.split("\\.");
            return split[split.length - 1].toLowerCase(Locale.ROOT) + "Impl";
        } else {
            return "";
        }
    }
}
