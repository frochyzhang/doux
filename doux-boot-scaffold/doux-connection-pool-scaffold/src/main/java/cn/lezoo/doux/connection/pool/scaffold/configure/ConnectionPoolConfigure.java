package cn.lezoo.doux.connection.pool.scaffold.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/7
 **/

@Configuration
@ConfigurationProperties(prefix = "cn.lezoo.connection.pool")
public class ConnectionPoolConfigure {
    /**
     * 连接池驱动。如：netty
     */
    private String connectionDriver;
    /**
     * 连接池底层实现：双列表/阻塞队列
     */
    private String connectionPoolType;
    /**
     * 连接检查服务别名，默认提供：defaultPingService
     */
    private String pingService;
    /**
     * 取连接的尝试次数，默认取10次
     */
    private String fetchTimes = "10";

    public String getConnectionDriver() {
        return connectionDriver;
    }

    public void setConnectionDriver(String connectionDriver) {
        this.connectionDriver = connectionDriver;
    }

    public String getConnectionPoolType() {
        return connectionPoolType;
    }

    public void setConnectionPoolType(String connectionPoolType) {
        this.connectionPoolType = connectionPoolType;
    }

    public String getPingService() {
        return pingService;
    }

    public void setPingService(String pingService) {
        this.pingService = pingService;
    }

    public String getFetchTimes() {
        return fetchTimes;
    }

    public void setFetchTimes(String fetchTimes) {
        this.fetchTimes = fetchTimes;
    }

    @Override
    public String toString() {
        return "ConnectionPoolConfigure{" +
                "connectionDriver='" + connectionDriver + '\'' +
                ", connectionPoolType='" + connectionPoolType + '\'' +
                ", pingService='" + pingService + '\'' +
                ", fetchTimes='" + fetchTimes + '\'' +
                '}';
    }
}
