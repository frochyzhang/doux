package cn.lezoo.doux.white.list.diversion.http.config;

/**
 * @author huanghf
 * @date 2024/3/26 20:00
 */
public class BackupFlagContext {
    private static final ThreadLocal<Boolean> THREAD_LOCAL = new ThreadLocal<>();

    public static void setBackupFlag(Boolean flag) {
        THREAD_LOCAL.set(flag);
    }

    public static Boolean getBackupFlag() {
        return THREAD_LOCAL.get();
    }

    public static void clearBackupFlag() {
        THREAD_LOCAL.remove();
    }
}
