package com.allinfinance.dev.gateway.raft.service;

import com.alipay.remoting.exception.CodecException;
import com.alipay.remoting.serialization.SerializerManager;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.entity.Task;
import com.alipay.sofa.jraft.error.RaftError;
import com.allinfinance.dev.gateway.listener.GatewayStartedListener;
import com.allinfinance.dev.gateway.raft.GatewayClosure;
import com.allinfinance.dev.gateway.raft.GatewayOperation;
import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

/**
 * @author huanghf
 * @date 2022/11/24 15:45
 */
@Service
public class GatewayServiceImpl implements GatewayService {
    private static final Logger logger = LoggerFactory.getLogger(GatewayServiceImpl.class);

    @Autowired
    private GatewayStartedListener gatewayServer;

    @Override
    public void register(RpcConfigurationProperties.Bootstrap bootstrap, GatewayClosure closure) {
        if (!isLeader()) {
            closure.failure();
            closure.run(new Status(RaftError.EAGAIN, "Not leader, please retry"));
            return;
        }

        try {
            GatewayOperation registerOperation = GatewayOperation.createRegister(bootstrap);
            closure.setGatewayOperation(registerOperation);
            Task task = new Task();
            task.setData(ByteBuffer.wrap(SerializerManager.getSerializer(SerializerManager.Hessian2).serialize(registerOperation)));
            task.setDone(closure);
            gatewayServer.getNode().apply(task);
        } catch (CodecException e) {
            logger.error("注册请求序列化失败", e);
            closure.failure();
            closure.run(new Status(RaftError.EAGAIN, "Fail to encode GatewayOperation"));
        }
    }

    @Override
    public void offline(String appUniqueId, GatewayClosure closure) {
        if (!isLeader()) {
            closure.failure();
            closure.run(new Status(RaftError.EAGAIN, "Not leader, please retry"));
            return;
        }

        try {
            GatewayOperation offlineRegisterOperation = GatewayOperation.createOffline(appUniqueId);
            closure.setGatewayOperation(offlineRegisterOperation);
            Task task = new Task();
            task.setData(ByteBuffer.wrap(SerializerManager.getSerializer(SerializerManager.Hessian2).serialize(offlineRegisterOperation)));
            task.setDone(closure);
            gatewayServer.getNode().apply(task);
        } catch (CodecException e) {
            logger.error("下线请求序列化失败", e);
            closure.failure();
            closure.run(new Status(RaftError.EAGAIN, "Fail to encode GatewayOperation"));
        }
    }

    private boolean isLeader() {
        return gatewayServer.getFsm().isLeader();
    }
}
