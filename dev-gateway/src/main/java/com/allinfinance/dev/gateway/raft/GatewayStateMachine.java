package com.allinfinance.dev.gateway.raft;

import com.alipay.remoting.exception.CodecException;
import com.alipay.remoting.serialization.SerializerManager;
import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.core.StateMachineAdapter;
import com.alipay.sofa.jraft.error.RaftError;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotReader;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotWriter;
import com.allinfinance.dev.gateway.factory.AppProcessFactory;
import com.allinfinance.dev.gateway.factory.GateClientFactoryAware;
import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author huanghf
 * @date 2022/11/23 14:43
 */
@Lazy
@Component
public class GatewayStateMachine extends StateMachineAdapter {
    private static final Logger logger = LoggerFactory.getLogger(GatewayStateMachine.class);

    /**
     * leader term
     */
    private final AtomicLong leaderTerm = new AtomicLong(-1);

    @Autowired
    private GateClientFactoryAware gateClientFactoryAware;
    @Autowired
    private AppProcessFactory appProcessFactory;

    @Value("${com.alipay.sofa.rpc.registry-address}")
    private String registryAddress;
    @Value("${dev.gateway.exporter-list}")
    private String exporterList;

    @Override
    public void onApply(Iterator iter) {
        while (iter.hasNext()) {
            GatewayClosure gatewayClosure = null;
            GatewayOperation gatewayOperation = null;
            if (iter.done() != null) {
                //leader节点
                gatewayClosure = (GatewayClosure) iter.done();
                gatewayOperation = gatewayClosure.getGatewayOperation();
            } else {
                //follower节点
                final ByteBuffer data = iter.getData();
                try {
                    gatewayOperation = SerializerManager.getSerializer(SerializerManager.Hessian2)
                            .deserialize(data.array(), GatewayOperation.class.getName());
                } catch (CodecException e) {
                    logger.error("Bootstrap反序列化失败", e);
                }
            }

            if (gatewayOperation != null) {
                RpcConfigurationProperties.Bootstrap bootstrap = gatewayOperation.getBootstrap();
                String appUniqueId = gatewayOperation.getAppUniqueId();
                switch (gatewayOperation.getOperation()) {
                    case GatewayOperation.REGISTER:
                        try {
                            if (gateClientFactoryAware.registerConsumer(bootstrap)) {
                                logger.info("[ {} ]应用注册成功!", bootstrap.getAppUniqueId());
                                if (gatewayClosure != null) {
                                    gatewayClosure.success();
                                    gatewayClosure.run(Status.OK());
                                }
                                break;
                            }
                        } catch (Exception e) {
                            logger.error("[ {} ]应用注册异常!", bootstrap.getAppUniqueId(), e);
                        }
                        logger.error("[ {} ]应用注册失败!", bootstrap.getAppUniqueId());
                        if (gatewayClosure != null) {
                            gatewayClosure.failure();
                            gatewayClosure.run(new Status(RaftError.EAGAIN, "注册失败，请重试"));
                        }
                        break;
                    case GatewayOperation.OFFLINE:
                        try {
                            boolean verifyResult = gateClientFactoryAware.verifyExporter(appUniqueId);
                            if (verifyResult) {
                                logger.info("[{}]应用未全部下线", appUniqueId);
                                if (gatewayClosure != null) {
                                    gatewayClosure.success();
                                    gatewayClosure.run(Status.OK());
                                }
                                break;
                            }
                            logger.info("所有[{}]的应用均已下线，移除网关订阅和配置信息，取消端口监听!", appUniqueId);
                            appProcessFactory.removeAll(appUniqueId);
                            if (gatewayClosure != null) {
                                gatewayClosure.success();
                                gatewayClosure.run(Status.OK());
                            }
                        } catch (Exception e) {
                            logger.error("[{}]应用下线异常", appUniqueId, e);
                            if (gatewayClosure != null) {
                                gatewayClosure.failure();
                                gatewayClosure.run(new Status(RaftError.EAGAIN, "下线失败，请重试"));
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
            iter.next();
        }
    }

    public boolean isLeader() {
        return leaderTerm.get() > 0;
    }

    @Override
    public void onSnapshotSave(SnapshotWriter writer, Closure done) {
        done.run(Status.OK());
    }

    @Override
    public boolean onSnapshotLoad(SnapshotReader reader) {
        logger.info("网关启动完成，开始订阅应用业务处理服务");
        String server = registryAddress.substring(registryAddress.indexOf("://") + 3);
        AppProcessFactory.getServiceList(server)
                .forEach(service -> {
                    String uniqueId = service.split(":")[1];
                    if (StringUtils.isBlank(exporterList) || exporterList.contains(uniqueId)) {
                        if (gateClientFactoryAware.registerConsumer(uniqueId)) {
                            logger.info("[ {} ]应用注册成功!", uniqueId);
                        } else {
                            logger.error("[ {} ]应用注册失败!", uniqueId);
                        }
                    }
                });
        logger.info("应用业务处理服务订阅完成");
        return true;
    }

    @Override
    public void onLeaderStart(long term) {
        this.leaderTerm.set(term);
        super.onLeaderStart(term);
    }

    @Override
    public void onLeaderStop(Status status) {
        this.leaderTerm.set(-1);
        super.onLeaderStop(status);
    }
}
