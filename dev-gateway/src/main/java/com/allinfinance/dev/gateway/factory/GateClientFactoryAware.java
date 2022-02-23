package com.allinfinance.dev.gateway.factory;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alipay.sofa.rpc.boot.runtime.param.BoltBindingParam;
import com.alipay.sofa.rpc.core.exception.SofaRouteException;
import com.alipay.sofa.runtime.api.aware.ClientFactoryAware;
import com.alipay.sofa.runtime.api.client.ClientFactory;
import com.alipay.sofa.runtime.api.client.ReferenceClient;
import com.alipay.sofa.runtime.api.client.param.ReferenceParam;
import com.allinfinance.dev.core.bean.MinaSocketBean;
import com.allinfinance.dev.gateway.netty.HttpServer;
import com.allinfinance.dev.gateway.util.AppPropertiesMapper;
import com.allinfinance.dev.rpc.scaffold.api.ProcessService;
import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;
import com.allinfinance.dev.socket.config.ShortSwitchServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/1/29 09:37
 */
@Service
public class GateClientFactoryAware implements ClientFactoryAware {
    private static final Logger logger = LoggerFactory.getLogger(GateClientFactoryAware.class);

    @Autowired
    private AppProcessFactory appProcessFactory;

    private ReferenceClient referenceClient;


    @Override
    public void setClientFactory(ClientFactory clientFactory) {

        this.referenceClient = clientFactory.getClient(ReferenceClient.class);

        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, "10.100.79.102:8848");
        properties.put(PropertyKeyConst.NAMESPACE, "public");
        try {
            NamingService namingService = NacosFactory.createNamingService(properties);

            namingService.getServicesOfServer(1, 10).getData().stream()
                    .filter(service -> service.contains(ProcessService.class.getName()))
                    .forEach(service -> {
                        String uniqueId = service.split(":")[1];
                        if (registerConsumer(uniqueId)) {
                            logger.info("[ {} ]应用注册成功!", uniqueId);
                        }
                    });
        } catch (NacosException e) {
            logger.error("【{}】应用注册失败！", clientFactory, e);
        }

    }

    /*
    1、重复注册的逻辑校验：需增加参数校验，参数不一致时需做参数刷新
    2、移除订阅并下掉端口监听
    3、网关异步同步应用注册信息
    4、去注册中心查询服务，
     */
    public Boolean registerConsumer(String uniqueId) {

        ReferenceParam<ProcessService> referenceParam = new ReferenceParam<>();
        BoltBindingParam boltBindingParam = new BoltBindingParam();
        boltBindingParam.setLoadBalancer("roundRobin");
        referenceParam.setBindingParam(boltBindingParam);

        referenceParam.setInterfaceType(ProcessService.class);
        referenceParam.setUniqueId(uniqueId);

        ProcessService processService = referenceClient.reference(referenceParam);
        try {
            if (processService.verify()) {
                logger.info("[ {} ]业务处理服务订阅成功!", uniqueId);
                RpcConfigurationProperties.Bootstrap bootstrap = processService.init();
                logger.info("应用注册参数:{}", bootstrap);
                if (appProcessFactory.checkIfExist(uniqueId, bootstrap)) {
                    logger.info("[ {} ]无需重复注册", uniqueId);
                    return Boolean.TRUE;
                }
                // 监听端口
                bootstrap.getAppList().forEach(appConfigList -> {
                    RpcConfigurationProperties.Bootstrap.AppConfigList.Type type = appConfigList.getType();
                    switch (type) {
                        case TCP:
                            MinaSocketBean minaSocketBean = AppPropertiesMapper.INSTANCE.convertToMinaSocketBean(appConfigList.getTcpConfig());
                            minaSocketBean.setName(uniqueId);
                            minaSocketBean.setPort(appConfigList.getListenPort());
                            minaSocketBean.setKeepAlive(false);
                            minaSocketBean.setSoLinger(false);
                            // 监听完成
                            try {
                                new ShortSwitchServer().initMinaServer(minaSocketBean);
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException | IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case HTTP:
                            Boolean startResult = new HttpServer().start(appConfigList.getListenPort(), appConfigList.getHttpConfig());
                            if (startResult) {
                                appProcessFactory.register(uniqueId, appConfigList.getHttpConfig().getUrlList());
                            }

                            break;
                        default:
                            throw new IllegalArgumentException("参数不合法");
                    }
                });

                appProcessFactory.register(uniqueId, processService);
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (SofaRouteException e) {
            logger.warn("[ {} ]已掉线,移除订阅!", uniqueId);
            referenceClient.removeReference(referenceParam);
            return Boolean.FALSE;
        }
    }

}
