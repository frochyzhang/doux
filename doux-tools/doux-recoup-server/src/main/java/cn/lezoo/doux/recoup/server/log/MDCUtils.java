package cn.lezoo.doux.recoup.server.log;

import cn.hutool.core.lang.UUID;
import org.slf4j.MDC;

/**
 * @author huanghf
 * @date 2023/10/10 16:42
 */
public class MDCUtils {
    public static final String TRACE_ID = "tid";

    public static void addTid() {
        MDC.put(TRACE_ID, UUID.randomUUID().toString(true));
    }

    public static void addTid(String tid) {
        MDC.put(TRACE_ID, tid);
    }

    public static String getTid() {
        return MDC.get(TRACE_ID);
    }

    public static void removeTid() {
        MDC.remove(TRACE_ID);
    }
}
