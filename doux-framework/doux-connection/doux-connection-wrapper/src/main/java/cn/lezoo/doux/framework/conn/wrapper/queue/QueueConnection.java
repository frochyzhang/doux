package cn.lezoo.doux.framework.conn.wrapper.queue;

import cn.lezoo.doux.framework.conn.driver.Connection;
import cn.lezoo.doux.framework.conn.wrapper.constant.enums.ConnectionStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ScheduledFuture;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/1
 **/
@Getter
@Setter
@Slf4j
public class QueueConnection implements InvocationHandler {
    private static final String CLOSE = "close";
    private static final String SEND = "send";
    private static final Class<?>[] IFACES = new Class<?>[]{Connection.class};

    private final int hashCode;
    private final QueueServerMetadata metadata;
    private final Connection realConnection;
    private final Connection proxyConnection;
    private long lastUsedTimestamp;
    private ConnectionStatus status;
    private ScheduledFuture<?> keepaliveTask;

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
        if (SEND.equals(methodName)) {
            try {
                return method.invoke(realConnection, args);
            } catch (Exception e) {
                log.error("请求发送异常", e);
                this.closeConnection();
                return null;
            }
        }
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
            log.error("方法[{}]执行异常", methodName, t);
            throw t;
        }
    }

    private void checkConnection() throws RuntimeException {
        if (!ConnectionStatus.ACTIVE.equals(status)) {
            throw new RuntimeException("Error accessing QueueConnection. Connection is invalid.");
        }
    }

    /**
     * Getter for the time since this connection was last used.
     *
     * @return - the time since the last use
     */
    public long getTimeElapsedSinceLastUse() {
        return System.currentTimeMillis() - lastUsedTimestamp;
    }

    public void closeConnection() {
        try {
            log.warn("连接关闭，id: {}, 距离上次使用时间: {}", this.getHashCode(), this.getTimeElapsedSinceLastUse());
            this.getRealConnection().close();
        } catch (Exception e2) {
            log.error("关闭连接异常", e2);
        }
        metadata.getUsedConnections().remove(this);
        this.setStatus(ConnectionStatus.INACTIVE);
        ScheduledFuture<?> keepaliveTask = this.getKeepaliveTask();
        if (keepaliveTask != null) {
            keepaliveTask.cancel(true);
            if (keepaliveTask.isCancelled()) {
                log.warn("keepalive任务已被取消");
            }
        }
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
