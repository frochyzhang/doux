package com.allinfinance.dev.gateway.listener;

import com.alipay.sofa.jraft.RaftGroupService;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.alipay.sofa.jraft.rpc.RaftRpcServerFactory;
import com.alipay.sofa.jraft.rpc.RpcServer;
import com.allinfinance.dev.gateway.config.GatewayClusterConfig;
import com.allinfinance.dev.gateway.config.RaftServerContext;
import com.allinfinance.dev.gateway.raft.AppRegistrarProcessor;
import com.allinfinance.dev.gateway.raft.ExporterOfflineProcessor;
import com.allinfinance.dev.gateway.raft.GatewayStateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author huanghf
 * @date 2022/4/26 19:33
 */
@Component
public class GatewayStartedListener implements ApplicationListener<ApplicationStartedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(GatewayStartedListener.class);

    @Autowired
    private RaftServerContext raftServerContext;
    @Autowired
    private GatewayClusterConfig gatewayClusterConfig;
    @Autowired
    private AppRegistrarProcessor appRegistrarProcessor;
    @Autowired
    private ExporterOfflineProcessor exporterOfflineProcessor;
    @Autowired
    private GatewayStateMachine gatewayStateMachine;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        logger.info("网关启动完成，开始加载raft节点配置");
        NodeOptions nodeOptions = new NodeOptions();
        nodeOptions.setElectionTimeoutMs(gatewayClusterConfig.getElectionTimoutMs());
        nodeOptions.setDisableCli(false);
        nodeOptions.setSnapshotIntervalSecs(gatewayClusterConfig.getSnapshotIntervalSec());

        PeerId peerId = new PeerId();
        if (!peerId.parse(gatewayClusterConfig.getServerAddr())) {
            throw new IllegalArgumentException("Fail to parse serverAddr");
        }
        Configuration cluster = new Configuration();
        if (!cluster.parse(gatewayClusterConfig.getClusterAddr())) {
            throw new IllegalArgumentException("Fail to parse clusterAddr");
        }
        nodeOptions.setInitialConf(cluster);

        RpcServer raftRpcServer = RaftRpcServerFactory.createRaftRpcServer(peerId.getEndpoint());
        raftRpcServer.registerProcessor(appRegistrarProcessor);
        raftRpcServer.registerProcessor(exporterOfflineProcessor);
        raftServerContext.setFsm(gatewayStateMachine);
        nodeOptions.setFsm(gatewayStateMachine);
        nodeOptions.setLogUri(gatewayClusterConfig.getDataPath() + File.separator + "log");
        nodeOptions.setRaftMetaUri(gatewayClusterConfig.getDataPath() + File.separator + "raft_meta");
        nodeOptions.setSnapshotUri(gatewayClusterConfig.getDataPath() + File.separator + "snapshot");
        raftServerContext.setRaftGroupService(new RaftGroupService(gatewayClusterConfig.getClusterGroupId(), peerId, nodeOptions, raftRpcServer));
        raftServerContext.start();
        logger.info("raft节点配置加载完成");
    }
}
