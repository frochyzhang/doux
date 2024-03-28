package com.allinfinance.dev.white.list.diversion.http.logging;

import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.action.ActionUtil;
import ch.qos.logback.core.joran.spi.ActionException;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.util.OptionHelper;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.xml.sax.Attributes;

import java.util.Optional;

/**
 * @author huanghf
 * @date 2024/3/27 15:10
 */
public class BackupPropertyAction extends Action {
    private static final String DEFAULT_VALUE_ATTRIBUTE = "defaultValue";
    private static final String SOURCE_ATTRIBUTE = "source";

    @Override
    public void begin(InterpretationContext interpretationContext, String s, Attributes attributes) throws ActionException {
        String name = attributes.getValue(NAME_ATTRIBUTE);
        String source = attributes.getValue(SOURCE_ATTRIBUTE);
        ActionUtil.Scope scope = ActionUtil.stringToScope(attributes.getValue(SCOPE_ATTRIBUTE));
        String defaultValue = attributes.getValue(DEFAULT_VALUE_ATTRIBUTE);
        if (OptionHelper.isEmpty(name) || OptionHelper.isEmpty(source)) {
            addError("The \"name\" and \"source\" attributes of <trafficBackupProperty> must be set");
        }
        ActionUtil.setProperty(interpretationContext, name, getValue(source, defaultValue), scope);
    }

    @Override
    public void end(InterpretationContext interpretationContext, String s) throws ActionException {

    }

    private String getValue(String source, String defaultValue) {
        String value = SpringUtil.getProperty(StrUtil.toCamelCase(source));
        if (StrUtil.isEmpty(value)) {
            value = Optional.ofNullable(SpringUtil.getProperty(StrUtil.toSymbolCase(source, '-')))
                    .orElse(defaultValue);
        }
        return value;
    }
}
