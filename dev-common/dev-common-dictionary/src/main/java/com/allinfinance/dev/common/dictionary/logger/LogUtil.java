package com.allinfinance.dev.common.dictionary.logger;

import cn.hutool.extra.spring.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.text.MessageFormat;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2021/7/3 02:16
 */
public class LogUtil {
    private static DesensitizedSpi desensitizedSpi = null;

    public static void debug(Logger logger, String template, Object... parameters) {
        String tpl = processTemplate(template);
        processParams(parameters);
        if (logger.isDebugEnabled()) {
            logger.debug(MessageFormat.format(tpl, parameters));
        }

    }

    public static void info(Logger logger, String template, Object... parameters) {
        String tpl = processTemplate(template);
        processParams(parameters);
        if (logger.isInfoEnabled()) {
            logger.info(MessageFormat.format(tpl, parameters));
        }

    }

    private static void processParams(Object[] parameters) {
        if (desensitizedSpi != null) {
            for (int i = 0; i < parameters.length; ++i) {
                String s = desensitizedSpi.convert(parameters[i]);
                if (s == null) {
                    return;
                }

                parameters[i] = s;
            }

        }
    }

    public static void warn(Logger logger, String template, Object... parameters) {
        String tpl = processTemplate(template);
        processParams(parameters);
        logger.warn(MessageFormat.format(tpl, parameters));
    }

    public static void warn(Throwable e, Logger logger, String template, Object... parameters) {
        String tpl = processTemplate(template);
        processParams(parameters);
        logger.warn(MessageFormat.format(tpl, parameters), e);
    }

    public static void error(Throwable e, Logger logger, String template, Object... parameters) {
        String tpl = processTemplate(template);
        processParams(parameters);
        logger.error(MessageFormat.format(tpl, parameters), e);
    }

    public static void error(Logger logger, String template, Object... parameters) {
        String tpl = processTemplate(template);
        processParams(parameters);
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


    static {
        try {
            desensitizedSpi = SpringUtil.getBean(DesensitizedSpi.class);
            System.out.println("脱敏服务注册成功" + desensitizedSpi.getClass().getName());
        } catch (Exception e) {
            System.err.println("脱敏服务注册失败" + e.getMessage());
        }
    }
}
