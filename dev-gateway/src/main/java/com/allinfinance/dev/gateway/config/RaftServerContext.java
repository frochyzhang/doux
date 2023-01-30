package com.allinfinance.dev.gateway.config;

import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.RaftGroupService;
import com.allinfinance.dev.gateway.raft.GatewayStateMachine;
import org.springframework.stereotype.Component;

/**
 * @author huanghf
 * @date 2023/1/19 14:32
 */
@Component
public class RaftServerContext {
    private RaftGroupService raftGroupService;
    private Node node;
    private GatewayStateMachine fsm;

    public void start() {
        this.node = this.raftGroupService.start();
    }

    public RaftGroupService getRaftGroupService() {
        return raftGroupService;
    }

    public void setRaftGroupService(RaftGroupService raftGroupService) {
        this.raftGroupService = raftGroupService;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public GatewayStateMachine getFsm() {
        return fsm;
    }

    public void setFsm(GatewayStateMachine fsm) {
        this.fsm = fsm;
    }

    @Override
    public String toString() {
        return "RaftServerContext{" +
                "raftGroupService=" + raftGroupService +
                ", node=" + node +
                ", fsm=" + fsm +
                '}';
    }
}
