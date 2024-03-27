package com.allinfinance.dev.white.list.diversion.http.util;

import com.allinfinance.dev.white.list.diversion.http.logging.BackupLogging;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author huanghf
 * @date 2024/3/27 14:52
 */
public class LogUtils {
    static {
        BackupLogging.getInstance().loadConfiguration();
    }

    public static Logger logger(Class<?> clazz) {
        return getLogger(clazz);
    }
}
