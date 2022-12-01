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
     * 节点地址
     */
    private String serverAddr;
    /**
     * 集群ID
     */
    private String clusterGroupId;
    /**
     * 集群地址列表
     */
    private String clusterAddr;
    /**
     * 自动 Snapshot 间隔时间，默认一个小时
     */
    private Integer snapshotIntervalSec = 3600;
    /**
     * follower等待leader发送心跳或者复制日志的时间，超过electionTimoutMs
     * 未收到leader的消息则会变为candidate发起选举或者等待leader出现。
     */
    private Integer electionTimoutMs = 1000;

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public String getClusterGroupId() {
        return clusterGroupId;
    }

    public void setClusterGroupId(String clusterGroupId) {
        this.clusterGroupId = clusterGroupId;
    }

    public String getClusterAddr() {
        return clusterAddr;
    }

    public void setClusterAddr(String clusterAddr) {
        this.clusterAddr = clusterAddr;
    }

    public Integer getSnapshotIntervalSec() {
        return snapshotIntervalSec;
    }

    public void setSnapshotIntervalSec(Integer snapshotIntervalSec) {
        this.snapshotIntervalSec = snapshotIntervalSec;
    }

    public Integer getElectionTimoutMs() {
        return electionTimoutMs;
    }

    public void setElectionTimoutMs(Integer electionTimoutMs) {
        this.electionTimoutMs = electionTimoutMs;
    }

    @Override
    public String toString() {
        return "GatewayClusterConfig{" +
                "dataPath='" + dataPath + '\'' +
                ", serverAddr='" + serverAddr + '\'' +
                ", clusterGroupId='" + clusterGroupId + '\'' +
                ", clusterAddr='" + clusterAddr + '\'' +
                ", snapshotIntervalSec=" + snapshotIntervalSec +
                ", electionTimoutMs=" + electionTimoutMs +
                '}';
    }
}
