package com.allinfinance.dev.connection.scaffold.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.text.MessageFormat;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2021/7/3 02:16
 */
public class LogUtils {

    public static void debug(Logger logger, String template, Object... parameters) {
        String tpl = processTemplate(template);
        if (logger.isDebugEnabled()) {
            logger.debug(MessageFormat.format(tpl, parameters));
        }

    }

    public static void info(Logger logger, String template, Object... parameters) {
        String tpl = processTemplate(template);
        if (logger.isInfoEnabled()) {
            logger.info(MessageFormat.format(tpl, parameters));
        }

    }

    public static void warn(Logger logger, String template, Object... parameters) {
        String tpl = processTemplate(template);
        logger.warn(MessageFormat.format(tpl, parameters));
    }

    public static void warn(Throwable e, Logger logger, String template, Object... parameters) {
        String tpl = processTemplate(template);
        logger.warn(MessageFormat.format(tpl, parameters), e);
    }

    public static void error(Throwable e, Logger logger, String template, Object... parameters) {
        String tpl = processTemplate(template);
        logger.error(MessageFormat.format(tpl, parameters), e);
    }

    public static void error(Logger logger, String template, Object... parameters) {
        String tpl = processTemplate(template);
        logger.error(MessageFormat.format(tpl, parameters));
    }

    private static String processTemplate(String template) {
        if (StringUtils.contains(template, "{}")) {
            for (int i = 0; i < 100; i++) {
                int index = template.indexOf("{}");
                if (index < 0) {
                    break;
                }
                template = StringUtils.replaceOnce(template, "{}", String.format("{%d}", i));
            }
        } else {
            template = StringUtils.appendIfMissing(template, ":{0}");
        }
        return template;
    }
}
