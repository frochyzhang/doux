/*
 * Copyright (C) 2014 Brett Wooldridge
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
package cn.lezoo.doux.framework.conn.wrapper.pool.impl;

import cn.lezoo.doux.framework.conn.driver.Connection;
import cn.lezoo.doux.framework.conn.wrapper.pool.util.ConcurrentBag.IConcurrentBagEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

import static cn.lezoo.doux.framework.conn.wrapper.pool.util.ClockSource.currentTime;
import static cn.lezoo.doux.framework.conn.wrapper.pool.util.ClockSource.elapsedDisplayString;
import static cn.lezoo.doux.framework.conn.wrapper.pool.util.ClockSource.elapsedMillis;

/**
 * Entry used in the ConcurrentBag to track Connection instances.
 *
 * @author Brett Wooldridge
 */
@Getter
@Setter
@Slf4j
final class PoolEntry implements IConcurrentBagEntry {

    private static final AtomicIntegerFieldUpdater<PoolEntry> stateUpdater;

    // real connection
    Connection connection;
    long lastAccessed;
    long lastBorrowed;

    @SuppressWarnings("FieldCanBeLocal")
    private volatile int state = 0;
    private volatile boolean evict;

    private volatile ScheduledFuture<?> endOfLife;
    private volatile ScheduledFuture<?> keepalive;

    private final HikariPool hikariPool;

    static {
        stateUpdater = AtomicIntegerFieldUpdater.newUpdater(PoolEntry.class, "state");
    }

    PoolEntry(final Connection connection, final PoolBase pool) {
        this.connection = connection;
        this.hikariPool = (HikariPool) pool;
        this.lastAccessed = currentTime();
    }

    /**
     * Release this entry back to the pool.
     */
    void recycle() {
        if (connection != null) {
            this.lastAccessed = currentTime();
            hikariPool.recycle(this);
        }
    }

    Connection createProxyConnection(final ProxyLeakTask leakTask) {
        return ProxyFactory.getProxyConnection(this, connection, leakTask);
    }

    String getPoolName() {
        return hikariPool.toString();
    }

    boolean isMarkedEvicted() {
        return evict;
    }

    void markEvicted() {
        this.evict = true;
    }

    void evict(final String closureReason) {
        hikariPool.closeConnection(this, closureReason);
    }

    /**
     * Returns millis since lastBorrowed
     */
    long getMillisSinceBorrowed() {
        return elapsedMillis(lastBorrowed);
    }

    PoolBase getPoolBase() {
        return hikariPool;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final long now = currentTime();
        return connection
                + ", accessed " + elapsedDisplayString(lastAccessed, now) + " ago, "
                + stateToString();
    }

    // ***********************************************************************
    //                      IConcurrentBagEntry methods
    // ***********************************************************************

    /**
     * {@inheritDoc}
     */
    @Override
    public int getState() {
        return stateUpdater.get(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean compareAndSet(int expect, int update) {
        return stateUpdater.compareAndSet(this, expect, update);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setState(int update) {
        stateUpdater.set(this, update);
    }

    Connection close() {
        ScheduledFuture<?> eol = endOfLife;
        if (eol != null && !eol.isDone() && !eol.cancel(false)) {
            log.warn("{} - maxLifeTime expiration task cancellation unexpectedly returned false for connection {}", getPoolName(), connection);
        }

        ScheduledFuture<?> ka = keepalive;
        if (ka != null && !ka.isDone() && !ka.cancel(false)) {
            log.warn("{} - keepalive task cancellation unexpectedly returned false for connection {}", getPoolName(), connection);
        }

        Connection con = connection;
        connection = null;
        endOfLife = null;
        keepalive = null;
        return con;
    }

    private String stateToString() {
        switch (state) {
            case STATE_IN_USE:
                return "IN_USE";
            case STATE_NOT_IN_USE:
                return "NOT_IN_USE";
            case STATE_REMOVED:
                return "REMOVED";
            case STATE_RESERVED:
                return "RESERVED";
            default:
                return "Invalid";
        }
    }
}
