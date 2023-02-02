package com.allinfinance.dev.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author huanghf
 * @date 2022/11/24 16:50
 */
@Component
@ConfigurationProperties(prefix = "com.allinfinance.gateway")
public class GatewayClusterConfig {
    /**
     * 日志、快照存储路径
     */
    private String dataPath;
    /**
     * 节点地址，ip:port
     */
    private String serverAddress;
    /**
     * 集群ID
     */
    private String clusterGroupId;
    /**
     * 集群地址列表，ip:port, ip:port
     */
    private String clusterAddress;
    /**
     * 自动 Snapshot 间隔时间，默认一个小时
     */
    private Integer snapshotIntervalSec = 3600;
    /**
     * follower等待leader发送心跳或者复制日志的时间，超过electionTimoutMs
     * 未收到leader的消息则会变为candidate发起选举或者等待leader出现。
     */
    private Integer electionTimeoutMs = 1000;

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getClusterGroupId() {
        return clusterGroupId;
    }

    public void setClusterGroupId(String clusterGroupId) {
        this.clusterGroupId = clusterGroupId;
    }

    public String getClusterAddress() {
        return clusterAddress;
    }

    public void setClusterAddress(String clusterAddress) {
        this.clusterAddress = clusterAddress;
    }

    public Integer getSnapshotIntervalSec() {
        return snapshotIntervalSec;
    }

    public void setSnapshotIntervalSec(Integer snapshotIntervalSec) {
        this.snapshotIntervalSec = snapshotIntervalSec;
    }

    public Integer getElectionTimeoutMs() {
        return electionTimeoutMs;
    }

    public void setElectionTimeoutMs(Integer electionTimeoutMs) {
        this.electionTimeoutMs = electionTimeoutMs;
    }

    @Override
    public String toString() {
        return "GatewayClusterConfig{" +
                "dataPath='" + dataPath + '\'' +
                ", serverAddress='" + serverAddress + '\'' +
                ", clusterGroupId='" + clusterGroupId + '\'' +
                ", clusterAddress='" + clusterAddress + '\'' +
                ", snapshotIntervalSec=" + snapshotIntervalSec +
                ", electionTimeoutMs=" + electionTimeoutMs +
                '}';
    }
}
