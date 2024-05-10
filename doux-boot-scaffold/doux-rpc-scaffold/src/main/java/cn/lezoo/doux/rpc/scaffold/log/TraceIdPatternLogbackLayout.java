package cn.lezoo.doux.rpc.scaffold.log;

import ch.qos.logback.classic.PatternLayout;

/**
 * @author huanghf
 * @date 2023/10/10 16:25
 */
public class TraceIdPatternLogbackLayout extends PatternLayout {
    static {
        defaultConverterMap.put("tid", LogbackPatternConverter.class.getName());
    }
}
