package cn.lezoo.doux.gateway.scaffold.bootstrap;

import cn.lezoo.doux.gateway.scaffold.api.ExporterOfflineRequest;
import cn.lezoo.doux.gateway.scaffold.api.ProcessService;
import cn.lezoo.doux.gateway.scaffold.config.Bootstrap;
import cn.lezoo.doux.gateway.scaffold.config.RaftRpcClientConfig;
import cn.lezoo.doux.gateway.scaffold.config.SofaAPIConfig;
import com.alibaba.nacos.api.exception.NacosException;
import com.alipay.sofa.rpc.config.ApplicationConfig;
import com.alipay.sofa.rpc.config.RegistryConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
import com.alipay.sofa.rpc.core.exception.SofaRouteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/1/27 16:36
 */
@ConditionalOnProperty(value = Bootstrap.BOOT_ENABLE, havingValue = "true")
@Configuration
public class RpcGatewayBootstrapRegistrar implements InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(RpcGatewayBootstrapRegistrar.class);

    @Autowired(required = false)
    private ProcessService processService;

    @Autowired
    private RaftRpcClientConfig raftRpcClientConfig;
    @Autowired
    private Bootstrap bootstrap;

    @Override
    public void afterPropertiesSet() throws NacosException {
        if (processService == null) {
            throw new NullPointerException("未匹配到ProcessService实现!");
        }
        if (StringUtils.hasText(bootstrap.getAppUniqueId())
                && StringUtils.hasText(bootstrap.getGateRegistry())) {

            RegistryConfig registryConfig = SofaAPIConfig.getRegistryConfig(bootstrap.getGateRegistry());

            // 1 processService注册到注册中心，需以uniqueId区分不同系统
            logger.info("开始发布应用{}的ProcessService服务到注册中心", bootstrap.getAppUniqueId());
            ApplicationConfig applicationConfig = new ApplicationConfig();
            applicationConfig.setAppName(bootstrap.getAppUniqueId());
            if (bootstrap.getExporterPort() == null) {
                throw new NullPointerException("exporter端口配置为空");
            }
            ServerConfig serverConfig = SofaAPIConfig.getServerConfig(bootstrap.getExporterPort());

            SofaAPIConfig.initProviderConfig(serverConfig, registryConfig, applicationConfig, bootstrap.getAppUniqueId(), processService);

            ProcessService testProcessService = SofaAPIConfig.referProxyConsumerRef(bootstrap.getAppUniqueId(),
                    registryConfig, ProcessService.class, "foreach");

            Boolean verifyResult = null;
            while (true) {
                try {
                    verifyResult = testProcessService.verify();
                } catch (SofaRouteException sofaRouteException) {
                    logger.warn("ProcessService未发布成功，等待10s后重试");
                }
                if (verifyResult != null) {
                    logger.info("{}业务处理服务发布成功", bootstrap.getAppUniqueId());
                    break;
                }
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    logger.error("调用本地验证服务失败!", e);
                    Thread.currentThread().interrupt();
                }
            }
        } else {
            throw new RuntimeException("TCP网关注册开关已打开，未设置必要参数!");
        }
    }

    @Override
    public void destroy() throws Exception {
        logger.info("开始调用网关下线服务");
        boolean offlineResult = raftRpcClientConfig.invokeSync(new ExporterOfflineRequest(bootstrap.getAppUniqueId()), 5000);
        if (offlineResult) {
            logger.info("调用网关下线服务成功");
        } else {
            logger.error("调用网关下线服务失败");
        }
    }
}
