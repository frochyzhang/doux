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
            log.error("异常", t);
            throw t;
        }
    }

    private void checkConnection() throws RuntimeException {
        if (!ConnectionStatus.ACTIVE.equals(status)) {
            throw new RuntimeException("Error accessing QueueConnection. Connection is invalid.");
        }
    }
}
