/*
 * Copyright (C) 2013, 2014 Brett Wooldridge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.lezoo.doux.framework.conn.wrapper.pool;

import cn.lezoo.doux.framework.conn.wrapper.pool.metrics.MetricsTrackerFactory;
import cn.lezoo.doux.framework.conn.wrapper.pool.util.PropertyElf;
import com.codahale.metrics.health.HealthCheckRegistry;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.security.AccessControlException;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;

import static cn.lezoo.doux.framework.conn.wrapper.pool.util.UtilityElf.getNullIfEmpty;
import static cn.lezoo.doux.framework.conn.wrapper.pool.util.UtilityElf.safeIsAssignableFrom;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

@Getter
@Setter
@Slf4j
@SuppressWarnings({"SameParameterValue", "unused"})
public class HikariConfig implements HikariConfigMXBean {

    private static final char[] ID_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final long CONNECTION_TIMEOUT = SECONDS.toMillis(30);
    private static final long VALIDATION_TIMEOUT = SECONDS.toMillis(5);
    private static final long SOFT_TIMEOUT_FLOOR = Long.getLong("cn.lezoo.doux.framework.conn.wrapper.pool.timeoutMs.floor", 250L);
    private static final long IDLE_TIMEOUT = MINUTES.toMillis(10);
    private static final long MAX_LIFETIME = MINUTES.toMillis(30);
    private static final long DEFAULT_KEEPALIVE_TIME = 0L;
    private static final int DEFAULT_POOL_SIZE = 10;

    private static boolean unitTest = false;

    // Properties changeable at runtime through the HikariConfigMXBean
    //
    private volatile long connectionTimeout;
    private volatile long validationTimeout;
    private volatile long idleTimeout;
    private volatile long leakDetectionThreshold;
    private volatile long maxLifetime;
    private volatile int maxPoolSize;
    private volatile int minIdle;

    // Properties NOT changeable at runtime
    //
    private long initializationFailTimeout;
    private String exceptionOverrideClassName;
    private String poolName;
    private boolean isRegisterMbeans;
    private boolean isAllowPoolSuspension;
    private Properties additionalProperties;
    /**
     * The thread factory used to create threads.
     * (maybe null, in which case the default thread factory is used)
     */
    private ThreadFactory threadFactory;
    /**
     * ScheduledExecutorService used for housekeeping.
     */
    private ScheduledExecutorService scheduledExecutor;
    private MetricsTrackerFactory metricsTrackerFactory;
    /**
     * MetricRegistry instance to use for registration of metrics used by HikariCP.  Default is {@code null}.
     */
    private Object metricRegistry;
    /**
     * HealthCheckRegistry that will be used for registration of health checks by HikariCP.  Currently only
     * Codahale/DropWizard is supported for health checks.
     */
    private Object healthCheckRegistry;
    private Properties healthCheckProperties;

    /**
     * This property controls the keepalive interval for a connection in the pool. An in-use connection will never be
     * tested by the keepalive thread, only when it is idle will it be tested.
     * <p>
     * the interval in which connections will be tested for aliveness, thus keeping them alive by the act of checking. Value is in milliseconds, default is 0 (disabled).
     */
    private long keepaliveTime;

    private volatile boolean sealed;

    /**
     * Default constructor
     * <p>
     * If the System property {@code hikari.configurationFile} is set,
     * then the default constructor will attempt to load the specified configuration file
     * <p>
     * {@link #HikariConfig(String propertyFileName)} can be similarly used
     * instead of using the system property
     */
    public HikariConfig() {
        healthCheckProperties = new Properties();

        minIdle = -1;
        maxPoolSize = -1;
        maxLifetime = MAX_LIFETIME;
        connectionTimeout = CONNECTION_TIMEOUT;
        validationTimeout = VALIDATION_TIMEOUT;
        idleTimeout = IDLE_TIMEOUT;
        keepaliveTime = DEFAULT_KEEPALIVE_TIME;

        String systemProp = System.getProperty("hikaricp.configurationFile");
        if (systemProp != null) {
            loadProperties(systemProp);
        }
    }

    /**
     * Construct a HikariConfig from the specified properties object.
     *
     * @param properties the name of the property file
     */
    public HikariConfig(Properties properties) {
        this();
        PropertyElf.setTargetFromProperties(this, properties);
    }

    /**
     * Construct a HikariConfig from the specified property file name.  <code>propertyFileName</code>
     * will first be treated as a path in the file-system, and if that fails the
     * Class.getResourceAsStream(propertyFileName) will be tried.
     *
     * @param propertyFileName the name of the property file
     */
    public HikariConfig(String propertyFileName) {
        this();

        loadProperties(propertyFileName);
    }

    // ***********************************************************************
    //                       HikariConfigMXBean methods
    // ***********************************************************************

    /**
     * {@inheritDoc}
     */
    @Override
    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConnectionTimeout(long connectionTimeoutMs) {
        if (connectionTimeoutMs == 0) {
            this.connectionTimeout = Integer.MAX_VALUE;
        } else if (connectionTimeoutMs < SOFT_TIMEOUT_FLOOR) {
            throw new IllegalArgumentException("connectionTimeout cannot be less than " + SOFT_TIMEOUT_FLOOR + "ms");
        } else {
            this.connectionTimeout = connectionTimeoutMs;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getIdleTimeout() {
        return idleTimeout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIdleTimeout(long idleTimeoutMs) {
        if (idleTimeoutMs < 0) {
            throw new IllegalArgumentException("idleTimeout cannot be negative");
        }
        this.idleTimeout = idleTimeoutMs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLeakDetectionThreshold() {
        return leakDetectionThreshold;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLeakDetectionThreshold(long leakDetectionThresholdMs) {
        this.leakDetectionThreshold = leakDetectionThresholdMs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getMaxLifetime() {
        return maxLifetime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMaxLifetime(long maxLifetimeMs) {
        this.maxLifetime = maxLifetimeMs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaximumPoolSize() {
        return maxPoolSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMaximumPoolSize(int maxPoolSize) {
        if (maxPoolSize < 1) {
            throw new IllegalArgumentException("maxPoolSize cannot be less than 1");
        }
        this.maxPoolSize = maxPoolSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinimumIdle() {
        return minIdle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMinimumIdle(int minIdle) {
        if (minIdle < 0) {
            throw new IllegalArgumentException("minimumIdle cannot be negative");
        }
        this.minIdle = minIdle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getValidationTimeout() {
        return validationTimeout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValidationTimeout(long validationTimeoutMs) {
        if (validationTimeoutMs < SOFT_TIMEOUT_FLOOR) {
            throw new IllegalArgumentException("validationTimeout cannot be less than " + SOFT_TIMEOUT_FLOOR + "ms");
        }

        this.validationTimeout = validationTimeoutMs;
    }

    // ***********************************************************************
    //                     All other configuration methods
    // ***********************************************************************

    /**
     * Get the pool suspension behavior (allowed or disallowed).
     *
     * @return the pool suspension behavior
     */
    public boolean isAllowPoolSuspension() {
        return isAllowPoolSuspension;
    }

    /**
     * Set whether or not pool suspension is allowed.  There is a performance
     * impact when pool suspension is enabled.  Unless you need it (for a
     * redundancy system for example) do not enable it.
     *
     * @param isAllowPoolSuspension the desired pool suspension allowance
     */
    public void setAllowPoolSuspension(boolean isAllowPoolSuspension) {
        checkIfSealed();
        this.isAllowPoolSuspension = isAllowPoolSuspension;
    }

    public void setMetricsTrackerFactory(MetricsTrackerFactory metricsTrackerFactory) {
        if (metricRegistry != null) {
            throw new IllegalStateException("cannot use setMetricsTrackerFactory() and setMetricRegistry() together");
        }

        this.metricsTrackerFactory = metricsTrackerFactory;
    }

    /**
     * Set a MetricRegistry instance to use for registration of metrics used by HikariCP.
     *
     * @param metricRegistry the MetricRegistry instance to use
     */
    public void setMetricRegistry(Object metricRegistry) {
        if (metricsTrackerFactory != null) {
            throw new IllegalStateException("cannot use setMetricRegistry() and setMetricsTrackerFactory() together");
        }

        if (metricRegistry != null) {
            metricRegistry = getObjectOrPerformJndiLookup(metricRegistry);

            if (!safeIsAssignableFrom(metricRegistry, "com.codahale.metrics.MetricRegistry")
                    && !(safeIsAssignableFrom(metricRegistry, "io.micrometer.core.instrument.MeterRegistry"))) {
                throw new IllegalArgumentException("Class must be instance of com.codahale.metrics.MetricRegistry or io.micrometer.core.instrument.MeterRegistry");
            }
        }

        this.metricRegistry = metricRegistry;
    }

    /**
     * Set the HealthCheckRegistry that will be used for registration of health checks by HikariCP.  Currently only
     * Codahale/DropWizard is supported for health checks.  Default is {@code null}.
     *
     * @param healthCheckRegistry the HealthCheckRegistry to be used
     */
    public void setHealthCheckRegistry(Object healthCheckRegistry) {
        checkIfSealed();

        if (healthCheckRegistry != null) {
            healthCheckRegistry = getObjectOrPerformJndiLookup(healthCheckRegistry);

            if (!(healthCheckRegistry instanceof HealthCheckRegistry)) {
                throw new IllegalArgumentException("Class must be an instance of com.codahale.metrics.health.HealthCheckRegistry");
            }
        }

        this.healthCheckRegistry = healthCheckRegistry;
    }

    public void setHealthCheckProperties(Properties healthCheckProperties) {
        checkIfSealed();
        this.healthCheckProperties.putAll(healthCheckProperties);
    }

    public void addHealthCheckProperty(String key, String value) {
        checkIfSealed();
        healthCheckProperties.setProperty(key, value);
    }

    /**
     * Determine whether HikariCP will self-register {@link HikariConfigMXBean} and {@link HikariPoolMXBean} instances
     * in JMX.
     *
     * @return {@code true} if HikariCP will register MXBeans, {@code false} if it will not
     */
    public boolean isRegisterMbeans() {
        return isRegisterMbeans;
    }

    /**
     * Configures whether HikariCP self-registers the {@link HikariConfigMXBean} and {@link HikariPoolMXBean} in JMX.
     *
     * @param register {@code true} if HikariCP should register MXBeans, {@code false} if it should not
     */
    public void setRegisterMbeans(boolean register) {
        checkIfSealed();
        this.isRegisterMbeans = register;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPoolName() {
        return poolName;
    }

    /**
     * Set the name of the connection pool.  This is primarily used in logging and JMX management consoles
     * to identify pools and pool configurations
     *
     * @param poolName the name of the connection pool to use
     */
    public void setPoolName(String poolName) {
        checkIfSealed();
        this.poolName = poolName;
    }

    /**
     * Set the ScheduledExecutorService used for housekeeping.
     *
     * @param executor the ScheduledExecutorService
     */
    public void setScheduledExecutor(ScheduledExecutorService executor) {
        checkIfSealed();
        this.scheduledExecutor = executor;
    }

    /**
     * Set the user supplied SQLExceptionOverride class name.
     *
     * @param exceptionOverrideClassName the user supplied SQLExceptionOverride class name
     * @see SQLExceptionOverride
     */
    public void setExceptionOverrideClassName(String exceptionOverrideClassName) {
        checkIfSealed();

        Class<?> overrideClass = attemptFromContextLoader(exceptionOverrideClassName);
        try {
            if (overrideClass == null) {
                overrideClass = this.getClass().getClassLoader().loadClass(exceptionOverrideClassName);
                log.debug("SQLExceptionOverride class {} found in the HikariConfig class classloader {}", exceptionOverrideClassName, this.getClass().getClassLoader());
            }
        } catch (ClassNotFoundException e) {
            log.error("Failed to load SQLExceptionOverride class {} from HikariConfig class classloader {}", exceptionOverrideClassName, this.getClass().getClassLoader());
        }

        if (overrideClass == null) {
            throw new RuntimeException("Failed to load SQLExceptionOverride class " + exceptionOverrideClassName + " in either of HikariConfig class loader or Thread context classloader");
        }

        try {
            overrideClass.getConstructor().newInstance();
            this.exceptionOverrideClassName = exceptionOverrideClassName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate class " + exceptionOverrideClassName, e);
        }
    }

    /**
     * Set the thread factory to be used to create threads.
     *
     * @param threadFactory the thread factory (setting to null causes the default thread factory to be used)
     */
    public void setThreadFactory(ThreadFactory threadFactory) {
        checkIfSealed();
        this.threadFactory = threadFactory;
    }

    void seal() {
        this.sealed = true;
    }

    /**
     * Copies the state of {@code this} into {@code other}.
     *
     * @param other Other {@link HikariConfig} to copy the state to.
     */
    public void copyStateTo(HikariConfig other) {
        for (Field field : HikariConfig.class.getDeclaredFields()) {
            if (!Modifier.isFinal(field.getModifiers())) {
                field.setAccessible(true);
                try {
                    field.set(other, field.get(this));
                } catch (Exception e) {
                    throw new RuntimeException("Failed to copy HikariConfig state: " + e.getMessage(), e);
                }
            }
        }

        other.sealed = false;
    }

    // ***********************************************************************
    //                          Private methods
    // ***********************************************************************

    private Class<?> attemptFromContextLoader(final String driverClassName) {
        final ClassLoader threadContextClassLoader = Thread.currentThread().getContextClassLoader();
        if (threadContextClassLoader != null) {
            try {
                final Class<?> driverClass = threadContextClassLoader.loadClass(driverClassName);
                log.debug("Driver class {} found in Thread context class loader {}", driverClassName, threadContextClassLoader);
                return driverClass;
            } catch (ClassNotFoundException e) {
                log.debug("Driver class {} not found in Thread context class loader {}, trying classloader {}",
                        driverClassName, threadContextClassLoader, this.getClass().getClassLoader());
            }
        }

        return null;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void validate() {
        if (poolName == null) {
            poolName = generatePoolName();
        } else if (isRegisterMbeans && poolName.contains(":")) {
            throw new IllegalArgumentException("poolName cannot contain ':' when used with JMX");
        }

        validateNumerics();

        if (log.isDebugEnabled() || unitTest) {
            logConfiguration();
        }
    }

    private void validateNumerics() {
        if (maxLifetime != 0 && maxLifetime < SECONDS.toMillis(30)) {
            log.warn("{} - maxLifetime is less than 30000ms, setting to default {}ms.", poolName, MAX_LIFETIME);
            maxLifetime = MAX_LIFETIME;
        }

        // keepalive time must larger than 30 seconds
        if (keepaliveTime != 0 && keepaliveTime < SECONDS.toMillis(30)) {
            log.warn("{} - keepaliveTime is less than 30000ms, disabling it.", poolName);
            keepaliveTime = DEFAULT_KEEPALIVE_TIME;
        }

        // keepalive time must be less than maxLifetime (if maxLifetime is enabled)
        if (keepaliveTime != 0 && maxLifetime != 0 && keepaliveTime >= maxLifetime) {
            log.warn("{} - keepaliveTime is greater than or equal to maxLifetime, disabling it.", poolName);
            keepaliveTime = DEFAULT_KEEPALIVE_TIME;
        }

        if (leakDetectionThreshold > 0 && !unitTest) {
            if (leakDetectionThreshold < SECONDS.toMillis(2) || (leakDetectionThreshold > maxLifetime && maxLifetime > 0)) {
                log.warn("{} - leakDetectionThreshold is less than 2000ms or more than maxLifetime, disabling it.", poolName);
                leakDetectionThreshold = 0;
            }
        }

        if (connectionTimeout < SOFT_TIMEOUT_FLOOR) {
            log.warn("{} - connectionTimeout is less than {}ms, setting to {}ms.", poolName, SOFT_TIMEOUT_FLOOR, CONNECTION_TIMEOUT);
            connectionTimeout = CONNECTION_TIMEOUT;
        }

        if (validationTimeout < SOFT_TIMEOUT_FLOOR) {
            log.warn("{} - validationTimeout is less than {}ms, setting to {}ms.", poolName, SOFT_TIMEOUT_FLOOR, VALIDATION_TIMEOUT);
            validationTimeout = VALIDATION_TIMEOUT;
        }

        if (maxPoolSize < 1) {
            maxPoolSize = DEFAULT_POOL_SIZE;
        }

        if (minIdle < 0 || minIdle > maxPoolSize) {
            minIdle = maxPoolSize;
        }

        if (idleTimeout + SECONDS.toMillis(1) > maxLifetime && maxLifetime > 0 && minIdle < maxPoolSize) {
            log.warn("{} - idleTimeout is close to or more than maxLifetime, disabling it.", poolName);
            idleTimeout = 0;
        } else if (idleTimeout != 0 && idleTimeout < SECONDS.toMillis(10) && minIdle < maxPoolSize) {
            log.warn("{} - idleTimeout is less than 10000ms, setting to default {}ms.", poolName, IDLE_TIMEOUT);
            idleTimeout = IDLE_TIMEOUT;
        } else if (idleTimeout != IDLE_TIMEOUT && idleTimeout != 0 && minIdle == maxPoolSize) {
            log.warn("{} - idleTimeout has been set but has no effect because the pool is operating as a fixed size pool.", poolName);
        }
    }

    private void checkIfSealed() {
        if (sealed)
            throw new IllegalStateException("The configuration of the pool is sealed once started. Use HikariConfigMXBean for runtime changes.");
    }

    private void logConfiguration() {
        log.debug("{} - configuration:", poolName);
        final Set<String> propertyNames = new TreeSet<>(PropertyElf.getPropertyNames(HikariConfig.class));
        for (String prop : propertyNames) {
            try {
                Object value = PropertyElf.getProperty(prop, this);
                if ("additionalProperties".equals(prop)) {
                    value = PropertyElf.copyProperties(additionalProperties);
                }

                log.debug("{}{}", (prop + "................................................").substring(0, 32), value);
            } catch (Exception e) {
                // continue
            }
        }
    }

    private void loadProperties(String propertyFileName) {
        final File propFile = new File(propertyFileName);
        try (final InputStream is = propFile.isFile() ? Files.newInputStream(propFile.toPath()) : this.getClass().getResourceAsStream(propertyFileName)) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                PropertyElf.setTargetFromProperties(this, props);
            } else {
                throw new IllegalArgumentException("Cannot find property file: " + propertyFileName);
            }
        } catch (IOException io) {
            throw new RuntimeException("Failed to read property file", io);
        }
    }

    private String generatePoolName() {
        final String prefix = "HikariPool-";
        try {
            // Pool number is global to the VM to avoid overlapping pool numbers in classloader scoped environments
            synchronized (System.getProperties()) {
                final String next = String.valueOf(Integer.getInteger("cn.lezoo.doux.framework.conn.wrapper.pool.pool_number", 0) + 1);
                System.setProperty("cn.lezoo.doux.framework.conn.wrapper.pool.pool_number", next);
                return prefix + next;
            }
        } catch (AccessControlException e) {
            // The SecurityManager didn't allow us to read/write system properties
            // so just generate a random pool number instead
            final ThreadLocalRandom random = ThreadLocalRandom.current();
            final StringBuilder buf = new StringBuilder(prefix);

            for (int i = 0; i < 4; i++) {
                buf.append(ID_CHARACTERS[random.nextInt(62)]);
            }

            log.info("assigned random pool name '{}' (security manager prevented access to system properties)", buf);

            return buf.toString();
        }
    }

    private Object getObjectOrPerformJndiLookup(Object object) {
        if (object instanceof String) {
            try {
                InitialContext initCtx = new InitialContext();
                return initCtx.lookup((String) object);
            } catch (NamingException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return object;
    }
}
