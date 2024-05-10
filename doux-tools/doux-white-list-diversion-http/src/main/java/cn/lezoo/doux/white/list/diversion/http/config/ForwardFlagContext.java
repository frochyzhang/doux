package cn.lezoo.doux.white.list.diversion.http.config;

/**
 * @author huanghf
 * @date 2024/3/20 22:12
 */
public class ForwardFlagContext {
    private static final ThreadLocal<Boolean> THREAD_LOCAL = new ThreadLocal<>();

    public static void setForwardFlag(Boolean flag) {
        THREAD_LOCAL.set(flag);
    }

    public static Boolean getForwardFlag() {
        return THREAD_LOCAL.get();
    }

    public static void clearForwardFlag() {
        THREAD_LOCAL.remove();
    }
}
