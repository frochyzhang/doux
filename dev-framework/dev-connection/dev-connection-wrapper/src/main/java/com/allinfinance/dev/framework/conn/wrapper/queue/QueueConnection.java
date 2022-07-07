package com.allinfinance.dev.framework.conn.wrapper.queue;

import com.allinfinance.dev.framework.conn.driver.Connection;
import com.allinfinance.dev.framework.conn.wrapper.constant.enums.ConnectionStatus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/1
 **/
public class QueueConnection implements InvocationHandler {
    private static final String CLOSE = "close";
    private static final Class<?>[] IFACES = new Class<?>[]{Connection.class};

    private final int hashCode;
    private final QueueServerMetadata metadata;
    private final Connection realConnection;
    private final Connection proxyConnection;
    private long lastUsedTimestamp;
    private ConnectionStatus status;

    public QueueConnection(QueueServerMetadata serverMetadata, Connection connection) {
        this.hashCode = connection.hashCode();
        this.realConnection = connection;
        this.metadata = serverMetadata;
        this.status = ConnectionStatus.ACTIVE;
        this.lastUsedTimestamp = System.currentTimeMillis();
        this.proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), IFACES, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (CLOSE.equals(methodName)) {
            metadata.pushConnection(this);
            return null;
        }
        try {
            if (!Object.class.equals(method.getDeclaringClass())) {
                checkConnection();
            }
            return method.invoke(realConnection, args);
        } catch (Throwable t) {
            throw t;
        }
    }

    public QueueServerMetadata getMetadata() {
        return metadata;
    }

    public Connection getRealConnection() {
        return realConnection;
    }

    public Connection getProxyConnection() {
        return proxyConnection;
    }

    public ConnectionStatus getStatus() {
        return status;
    }

    public void setStatus(ConnectionStatus status) {
        this.status = status;
    }

    public long getLastUsedTimestamp() {
        return lastUsedTimestamp;
    }

    public void setLastUsedTimestamp(long lastUsedTimestamp) {
        this.lastUsedTimestamp = lastUsedTimestamp;
    }

    private void checkConnection() throws RuntimeException {
        if (!ConnectionStatus.ACTIVE.equals(status)) {
            throw new RuntimeException("Error accessing QueueConnection. Connection is invalid.");
        }
    }
}
