package cn.lezoo.doux.connection.pool.scaffold.configure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/7
 **/
@Getter
@Setter
@ToString
public class ServerMetadataConfigure {
    /**
     * 服务端ip
     */
    private String serverIp;
    /**
     * 服务端端口
     */
    private String serverPort;
    /**
     * 最大活跃连接数
     */
    private String maxActiveConnections;
    /**
     * 最大空闲连接数
     */
    private String maxIdleConnections;
    /**
     * 最大空闲连接检查时间，超过该时间后要检查连接活跃度，单位：毫秒
     */
    private String maxCheckoutTime;
    /**
     * 最大的请求超时时间，超过该时间表示请求超时，单位：毫秒
     */
    private String defaultNetworkTimeout;
    /**
     * 连接重试等待时间，单位：毫秒
     */
    private String retryTimeToWait;
    /**
     * 本地能容忍的最大坏连接数
     */
    private String maxLocalBadConnectionTolerance;
    /**
     * ping请求内容
     */
    private String pingQueryContent;
    /**
     * ping验证内容
     */
    private String pingVerifyContent;
    /**
     * ping开关
     */
    private String pingEnabled;
    /**
     * 最近一次使用该连接的时间差，单位：毫秒
     */
    private String pingConnectionsNotUsed;
    /**
     * 长度域长度，默认为: 2
     */
    private String lengthField;
    /**
     * 缓冲区大小，默认为: 65535
     */
    private String bufferSize;
    /**
     * 连接补充间隔
     */
    private String houseKeepInterval;
}
