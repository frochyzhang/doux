package cn.lezoo.doux.white.list.diversion.http.logging;

import cn.lezoo.doux.white.list.diversion.http.util.ConvertUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @author huanghf
 * @date 2024/3/27 15:09
 */
public abstract class AbstractBackupLogging {
    private static final String BACKUP_LOGGING_CONFIG_PROPERTY = "backup.logging.config";
    private static final String BACKUP_LOGGING_DEFAULT_CONFIG_ENABLED_PROPERTY = "backup.logging.default.config.enabled";
    private static final String BACKUP_LOGGING_PATH_DIR = "logs";

    static {
        String loggingPath = System.getProperty(SysEnv.JM_LOG_PATH);
        if (StringUtils.isBlank(loggingPath)) {
            String userHome = System.getProperty(SysEnv.USER_HOME);
            System.setProperty(SysEnv.JM_LOG_PATH, userHome + File.separator + BACKUP_LOGGING_PATH_DIR);
        }
    }

    protected String getLocation(String defaultLocation) {
        String location = System.getProperty(BACKUP_LOGGING_CONFIG_PROPERTY);
        if (StringUtils.isBlank(location)) {
            if (isDefaultConfigEnabled()) {
                return defaultLocation;
            }
            return null;
        }
        return location;
    }

    private boolean isDefaultConfigEnabled() {
        String property = System.getProperty(BACKUP_LOGGING_DEFAULT_CONFIG_ENABLED_PROPERTY);
        // The default value is true.
        return property == null || ConvertUtils.toBoolean(property);
    }

    /**
     * Load logging configuration.
     */
    public abstract void loadConfiguration();
}
