package cn.lezoo.doux.white.list.diversion.http.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.CoreConstants;
import cn.lezoo.doux.white.list.diversion.http.util.ResourceUtils;
import org.slf4j.impl.StaticLoggerBinder;

/**
 * @author huanghf
 * @date 2024/3/27 15:08
 */
public class LogbackBackupLogging extends AbstractBackupLogging {
    private static final String BACKUP_LOGBACK_LOCATION = "classpath:backup-logback.xml";

    @Override
    public void loadConfiguration() {
        LoggerContext loggerContext = loadConfigurationOnStart();
        if (loggerContext.getObject(CoreConstants.RECONFIGURE_ON_CHANGE_TASK) != null && !hasListener(loggerContext)) {
            addListener(loggerContext);
        }
    }

    private boolean hasListener(LoggerContext loggerContext) {
        for (LoggerContextListener loggerContextListener : loggerContext.getCopyOfListenerList()) {
            if (loggerContextListener instanceof BackupLoggerContextListener) {
                return true;
            }
        }
        return false;
    }

    private LoggerContext loadConfigurationOnStart() {
        String location = getLocation(BACKUP_LOGBACK_LOCATION);
        try {
            LoggerContext loggerContext = (LoggerContext) StaticLoggerBinder.getSingleton().getLoggerFactory();
            BackupJoranConfigurator configurator = new BackupJoranConfigurator();
            configurator.setContext(loggerContext);
            configurator.doBackupConfigure(ResourceUtils.getResourceUrl(location));
            return loggerContext;
        } catch (Exception e) {
            throw new IllegalStateException("Could not initialize Logback Nacos logging from " + location, e);
        }
    }

    class BackupLoggerContextListener implements LoggerContextListener {
        @Override
        public boolean isResetResistant() {
            return true;
        }

        @Override
        public void onReset(LoggerContext context) {
            loadConfigurationOnStart();
        }

        @Override
        public void onStart(LoggerContext context) {

        }

        @Override
        public void onStop(LoggerContext context) {

        }

        @Override
        public void onLevelChange(Logger logger, Level level) {

        }
    }

    private void addListener(LoggerContext loggerContext) {
        loggerContext.addListener(new BackupLoggerContextListener());
    }
}
