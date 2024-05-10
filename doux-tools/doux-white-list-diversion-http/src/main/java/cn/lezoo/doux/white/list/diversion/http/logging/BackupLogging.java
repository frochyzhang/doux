package cn.lezoo.doux.white.list.diversion.http.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huanghf
 * @date 2024/3/27 15:08
 */
public class BackupLogging {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackupLogging.class);

    private AbstractBackupLogging backupLogging;

    private boolean isLogback = false;

    private BackupLogging() {
        try {
            Class.forName("ch.qos.logback.classic.Logger");
            backupLogging = new LogbackBackupLogging();
            isLogback = true;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("logback依赖不存在");
            // backupLogging = new Log4J2BackupLogging();
        }
    }

    private static class BackupLoggingInstance {

        private static final BackupLogging INSTANCE = new BackupLogging();
    }

    public static BackupLogging getInstance() {
        return BackupLoggingInstance.INSTANCE;
    }

    /**
     * Load logging Configuration.
     */
    public void loadConfiguration() {
        try {
            backupLogging.loadConfiguration();
        } catch (Throwable t) {
            if (isLogback) {
                LOGGER.warn("Load Logback Configuration of Nacos fail, message: {}", t.getMessage());
            } else {
                LOGGER.warn("Load Log4j Configuration of Nacos fail, message: {}", t.getMessage());
            }
        }
    }
}
