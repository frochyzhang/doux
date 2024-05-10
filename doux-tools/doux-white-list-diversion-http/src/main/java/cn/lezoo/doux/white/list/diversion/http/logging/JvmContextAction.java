package cn.lezoo.doux.white.list.diversion.http.logging;

import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.action.ActionUtil;
import ch.qos.logback.core.joran.spi.ActionException;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.util.OptionHelper;
import org.xml.sax.Attributes;

import java.lang.management.ManagementFactory;

/**
 * @author huanghf
 * @date 2024/3/28 15:06
 */
public class JvmContextAction extends Action {
    private static final String PID_ATTRIBUTE = "pid";

    @Override
    public void begin(InterpretationContext ic, String s, Attributes attributes) throws ActionException {
        String name = attributes.getValue(NAME_ATTRIBUTE);
        ActionUtil.Scope scope = ActionUtil.stringToScope(attributes.getValue(SCOPE_ATTRIBUTE));
        if (OptionHelper.isEmpty(name)) {
            addError("The \"name\" attributes of <jvmContext> must be set");
        }
        ActionUtil.setProperty(ic, name, getValue(name), scope);
    }

    @Override
    public void end(InterpretationContext ic, String name) throws ActionException {

    }

    private String getValue(String name) {
        switch (name) {
            case PID_ATTRIBUTE:
                String jvmName = ManagementFactory.getRuntimeMXBean().getName();
                return jvmName.split("@")[0];
            default:
                return name + "_is_undefined";
        }
    }
}
