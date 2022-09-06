package com.allinfinance.dev.core.util.logger;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2021/7/3 02:17
 */
@Deprecated
public interface DesensitizedSpi {
    /**
     * 数据脱敏
     *
     * @param data 待脱敏数据
     * @return 脱敏后数据
     */
    String convert(Object data);
}
