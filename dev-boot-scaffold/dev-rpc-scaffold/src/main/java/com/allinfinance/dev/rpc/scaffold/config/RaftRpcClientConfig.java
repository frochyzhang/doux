package com.allinfinance.dev.rpc.scaffold.config;

import com.alipay.sofa.jraft.RouteTable;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.error.RemotingException;
import com.alipay.sofa.jraft.option.CliOptions;
import com.alipay.sofa.jraft.rpc.RpcClient;
import com.alipay.sofa.jraft.rpc.impl.cli.CliClientServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeoutException;

/**
 * @author huanghf
 * @date 2022/11/29 19:17
 */
@ConditionalOnProperty(value = RpcConfigurationProperties.Bootstrap.BOOT_ENABLE, havingValue = "true")
@Configuration
public class RaftRpcClientConfig implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(RaftRpcClientConfig.class);

    @Autowired
    private RpcConfigurationProperties rpcConfigurationProperties;

    private CliClientServiceImpl cliClientService;

    @Override
    public void afterPropertiesSet() throws Exception {
        final String groupId = rpcConfigurationProperties.getBootstrap().getGroupId();
        final String confStr = rpcConfigurationProperties.getBootstrap().getGateClusterAddress();

        final com.alipay.sofa.jraft.conf.Configuration conf = new com.alipay.sofa.jraft.conf.Configuration();
        if (!conf.parse(confStr)) {
            throw new IllegalArgumentException("Fail to parse conf:" + confStr);
        }

        RouteTable.getInstance().updateConfiguration(groupId, conf);

        this.cliClientService = new CliClientServiceImpl();
        cliClientService.init(new CliOptions());
    }

    public RpcClient getRpcClient() {
        try {
            if (!RouteTable.getInstance().refreshLeader(cliClientService, rpcConfigurationProperties.getBootstrap().getGroupId(), 1000).isOk()) {
                logger.error("Refresh leader failed");
                return null;
            }
        } catch (InterruptedException | TimeoutException e) {
            logger.error("Get client failed", e);
            return null;
        }

        final PeerId leader = RouteTable.getInstance().selectLeader(rpcConfigurationProperties.getBootstrap().getGroupId());
        logger.info("Leader is {}:{}", leader.getIp(), leader.getPort());
        return cliClientService.getRpcClient();
    }

    public <T> T invokeSync(Object request, long timoutMills) throws TimeoutException, InterruptedException, RemotingException {
        if (!RouteTable.getInstance().refreshLeader(cliClientService, rpcConfigurationProperties.getBootstrap().getGroupId(), 1000).isOk()) {
            throw new IllegalStateException("Refresh leader failed");
        }

        PeerId leader = RouteTable.getInstance().selectLeader(rpcConfigurationProperties.getBootstrap().getGroupId());
        logger.info("Leader is {}:{}", leader.getIp(), leader.getPort());
        return (T) cliClientService.getRpcClient().invokeSync(leader.getEndpoint(), request, timoutMills);
    }
}
