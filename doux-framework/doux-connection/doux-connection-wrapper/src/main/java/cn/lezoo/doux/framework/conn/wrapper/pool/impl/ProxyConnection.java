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

package cn.lezoo.doux.framework.conn.wrapper.pool.impl;

import cn.lezoo.doux.framework.conn.driver.Connection;
import cn.lezoo.doux.framework.conn.wrapper.pool.exception.PoolException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the proxy class for java.sql.Connection.
 *
 * @author Brett Wooldridge
 */
public abstract class ProxyConnection implements Connection {
    static final int DIRTY_BIT_READONLY = 0b000001;
    static final int DIRTY_BIT_AUTOCOMMIT = 0b000010;
    static final int DIRTY_BIT_ISOLATION = 0b000100;
    static final int DIRTY_BIT_CATALOG = 0b001000;
    static final int DIRTY_BIT_NETTIMEOUT = 0b010000;
    static final int DIRTY_BIT_SCHEMA = 0b100000;

    private static final Logger LOGGER;
    private static final Set<String> ERROR_STATES;
    private static final Set<Integer> ERROR_CODES;

    @SuppressWarnings("WeakerAccess")
    protected Connection delegate;

    private final PoolEntry poolEntry;
    private final ProxyLeakTask leakTask;

    private int dirtyBits;
    private boolean isCommitStateDirty;

    // static initializer
    static {
        LOGGER = LoggerFactory.getLogger(ProxyConnection.class);

        ERROR_STATES = new HashSet<>();
        ERROR_STATES.add("0A000"); // FEATURE UNSUPPORTED
        ERROR_STATES.add("57P01"); // ADMIN SHUTDOWN
        ERROR_STATES.add("57P02"); // CRASH SHUTDOWN
        ERROR_STATES.add("57P03"); // CANNOT CONNECT NOW
        ERROR_STATES.add("01002"); // SQL92 disconnect error
        ERROR_STATES.add("JZ0C0"); // Sybase disconnect error
        ERROR_STATES.add("JZ0C1"); // Sybase disconnect error

        ERROR_CODES = new HashSet<>();
        ERROR_CODES.add(500150);
        ERROR_CODES.add(2399);
        ERROR_CODES.add(1105);
    }

    protected ProxyConnection(final PoolEntry poolEntry,
        final Connection connection,
        final ProxyLeakTask leakTask) {
        this.poolEntry = poolEntry;
        this.delegate = connection;
        this.leakTask = leakTask;
    }

    /** {@inheritDoc} */
    @Override
    public final String toString() {
        return this.getClass().getSimpleName() + '@' + System.identityHashCode(this) + " wrapping " + delegate;
    }

    // ***********************************************************************
    //                          Internal methods
    // ***********************************************************************

    final PoolEntry getPoolEntry() {
        return poolEntry;
    }

    void cancelLeakTask() {
        leakTask.cancel();
    }

    /** {@inheritDoc} */
    @Override
    public final void close() {
        if (delegate != ClosedConnection.CLOSED_CONNECTION) {
            leakTask.cancel();

            try {
                if (dirtyBits != 0) {
                    //                           poolEntry.resetConnectionState(this, dirtyBits);
                }

                //                        delegate.clearWarnings();
            } catch (PoolException e) {
                // when connections are aborted, exceptions are often thrown that should not reach the application
                if (!poolEntry.isMarkedEvicted()) {
                    //                           throw checkException(e);
                }
            } finally {
                delegate = ClosedConnection.CLOSED_CONNECTION;
                poolEntry.recycle();
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("RedundantThrows")
    public boolean isClosed() throws PoolException {
        return (delegate == ClosedConnection.CLOSED_CONNECTION);
    }

    // **********************************************************************
    //                         Private classes
    // **********************************************************************


    private static final class ClosedConnection {
        static final Connection CLOSED_CONNECTION = getClosedConnection();

        private static Connection getClosedConnection() {
            InvocationHandler handler = (proxy, method, args) -> {
                final String methodName = method.getName();
                switch (methodName) {
                    case "isClosed":
                        return Boolean.TRUE;
                    case "isValid":
                        return Boolean.FALSE;
                    case "abort":
                    case "close":
                        return Void.TYPE;
                    case "toString":
                        return ClosedConnection.class.getCanonicalName();
                }

                throw new PoolException("Connection is closed");
            };

            return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[] {Connection.class}, handler);
        }
    }
}
