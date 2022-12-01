package com.allinfinance.dev.gateway.raft;

import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.RaftGroupService;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.alipay.sofa.jraft.rpc.RaftRpcServerFactory;
import com.alipay.sofa.jraft.rpc.RpcServer;
import com.allinfinance.dev.gateway.config.GatewayClusterConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 * @author huanghf
 * @date 2022/11/24 9:38
 */
//@Component
public class GatewayServer implements InitializingBean {
    private RaftGroupService raftGroupService;
    private Node node;
    private GatewayStateMachine fsm;

    @Autowired
    private GatewayClusterConfig gatewayClusterConfig;
    @Autowired
    private AppRegistrarProcessor appRegistrarProcessor;
    @Autowired
    private GatewayStateMachine gatewayStateMachine;

    public RaftGroupService getRaftGroupService() {
        return raftGroupService;
    }

    public Node getNode() {
        return node;
    }

    public GatewayStateMachine getFsm() {
        return fsm;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        NodeOptions nodeOptions = new NodeOptions();
        nodeOptions.setElectionTimeoutMs(1000);
        nodeOptions.setDisableCli(false);
        nodeOptions.setSnapshotIntervalSecs(30);

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
        this.fsm = gatewayStateMachine;
        nodeOptions.setFsm(gatewayStateMachine);
        nodeOptions.setLogUri(gatewayClusterConfig.getDataPath() + File.separator + "log");
        nodeOptions.setRaftMetaUri(gatewayClusterConfig.getDataPath() + File.separator + "raft_meta");
        nodeOptions.setSnapshotUri(gatewayClusterConfig.getDataPath() + File.separator + "snapshot");
        this.raftGroupService = new RaftGroupService(gatewayClusterConfig.getClusterGroupId(), peerId, nodeOptions, raftRpcServer);
        this.node = this.raftGroupService.start();
    }
}
