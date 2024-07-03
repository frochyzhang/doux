package cn.lezoo.doux.recoup.server.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.commons.lang3.StringUtils;

public class LogbackPatternConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent event) {
        StringBuilder tidBuilder = new StringBuilder("TID: ");
        String tid = MDCUtils.getTid();
        if (StringUtils.isNotBlank(tid)) {
            tidBuilder.append(tid);
        } else {
            tidBuilder.append("N/A");
        }
        return tidBuilder.toString();
    }

}
