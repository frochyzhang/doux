package com.allinfinance.dev.gateway.factory;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

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

    /**
     * 网关拉取provider列表时的重试次数
     */
    @Value("${dev.gateway.retries:3}")
    private Integer retries;

    /**
     * 网关拉取provider列表时的重试间隔，单位毫秒
     */
    @Value("${dev.gateway.interval:300}")
    private Integer interval;

    @Override
    public void setClientFactory(ClientFactory clientFactory) {
        this.referenceClient = clientFactory.getClient(ReferenceClient.class);
    }

    /**
     * 验证exporter是否正常（下线）
     *
     * @param uniqueId
     * @return
     */
    public boolean verifyExporter(String uniqueId) {
        logger.info("开始验证[{}]应用是否正常", uniqueId);
        ReferenceParam<ProcessService> processServiceParam = getProcessServiceParam(uniqueId);
        try {
            ProcessService processService = referenceClient.reference(processServiceParam);
            return processService.verify();
        } catch (Exception e) {
            logger.info("[{}]应用异常", uniqueId);
            return false;
        }
    }

    /**
     * 网关重启时调用，订阅exporter业务处理服务
     *
     * @param uniqueId ProcessService uniqueId
     * @return 是否成功
     */
    public boolean registerConsumer(String uniqueId) {
        logger.info("开始订阅[ {} ]业务处理服务", uniqueId);
        ReferenceParam<ProcessService> processServiceParam = getProcessServiceParam(uniqueId);
        for (int r = 1; r <= retries; r++) {
            ProcessService processService = referenceClient.reference(processServiceParam);
            try {
                if (processService.verify()) {
                    logger.info("[ {} ]业务处理服务验证成功，开始监听端口", uniqueId);
                    appProcessFactory.registerProcessService(uniqueId, processService);
                    RpcConfigurationProperties.Bootstrap bootstrap = processService.init();
                    logger.info("应用注册参数: {}", bootstrap);
                    // 监听端口
                    monitorPort(bootstrap);
                    return true;
                }
            } catch (SofaRouteException e) {
                logger.warn("调用[ {} ]应用ProcessService服务异常，等待重试...", uniqueId);
                logger.info("总重试次数：{}，重试间隔：{}ms，当前重试次数：{}", retries, interval, r);
                try {
                    TimeUnit.MILLISECONDS.sleep(interval);
                } catch (InterruptedException ignore) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e) {
                logger.error("订阅[ {} ]业务处理服务异常,移除订阅、端口监听和配置信息!", uniqueId, e);
                appProcessFactory.removeAll(uniqueId);
                return false;
            }
        }
        logger.error("调用[ {} ]应用ProcessService服务异常,移除订阅、端口监听和配置信息!", uniqueId);
        appProcessFactory.removeAll(uniqueId);
        return false;
    }

    /*
    应用前置调用
    1、重复注册的逻辑校验：需增加参数校验，参数不一致时需做参数刷新
    2、移除订阅并下掉端口监听
    3、网关异步同步应用注册信息
    4、去注册中心查询服务
     */
    public Boolean registerConsumer(RpcConfigurationProperties.Bootstrap bootstrap) {
        String uniqueId = bootstrap.getAppUniqueId();
        logger.info("开始订阅[ {} ]业务处理服务", uniqueId);
        logger.info("应用注册参数:{}", bootstrap);
        //判断应用的ProcessService是否存在且
        if (appProcessFactory.checkProcessServiceIfExist(uniqueId)) {
            //存在则判断保存的配置和送的是否一致
            if (appProcessFactory.checkBootstrapIfEqual(bootstrap)) {
                //一致则直接返回成功
                logger.info("[ {} ]无需重复注册", uniqueId);
            } else {
                logger.info("[ {} ]应用配置信息已更改，开始重新配置", uniqueId);
                //不一致则移除端口监听和配置信息，重新保存配置信息，监听端口
                appProcessFactory.removePortMonitor(uniqueId);
                //移除配置信息，包括compares和appUrlMap
                appProcessFactory.removeBootstrap(uniqueId);
                //重新保存配置信息
                appProcessFactory.registerBootstrap(bootstrap);
                //监听端口
                monitorPort(bootstrap);
            }
        } else {
            logger.info("[ {} ]应用首次注册，开始保存配置信息，监听端口", uniqueId);
            //不存在则保存ProcessService和配置信息，监听端口
            ReferenceParam<ProcessService> processServiceParam = getProcessServiceParam(uniqueId);
            for (int i = 1; i <= retries; i++) {
                ProcessService processService = referenceClient.reference(processServiceParam);
                try {
                    if (processService.verify()) {
                        logger.info("[ {} ]业务处理服务验证成功，开始监听端口!", uniqueId);
                        appProcessFactory.registerProcessService(bootstrap.getAppUniqueId(), processService);
                        monitorPort(bootstrap);
                        logger.info("[ {} ]业务处理服务订阅成功", uniqueId);
                        return Boolean.TRUE;
                    }
                } catch (SofaRouteException e) {
                    logger.warn("调用[ {} ]应用ProcessService服务异常，等待重试...", uniqueId);
                    logger.info("总重试次数：{}，重试间隔：{}ms，当前重试次数：{}", retries, interval, i);
                    try {
                        TimeUnit.MILLISECONDS.sleep(interval);
                    } catch (InterruptedException ignore) {
                        Thread.currentThread().interrupt();
                    }
                } catch (Exception e) {
                    logger.error("订阅[ {} ]业务处理服务异常,移除订阅、端口监听和配置信息!", uniqueId, e);
                    appProcessFactory.removeAll(uniqueId);
                    return false;
                }
            }
            logger.error("调用[ {} ]应用ProcessService服务异常，重试失败，移除订阅、端口监听和配置信息！", uniqueId);
            appProcessFactory.removeAll(uniqueId);
            return Boolean.FALSE;
        }
        logger.info("[ {} ]业务处理服务订阅成功", uniqueId);
        return Boolean.TRUE;
    }

    private ReferenceParam<ProcessService> getProcessServiceParam(String uniqueId) {
        ReferenceParam<ProcessService> referenceParam = new ReferenceParam<>();
        BoltBindingParam boltBindingParam = new BoltBindingParam();
        boltBindingParam.setLoadBalancer("roundRobin");
        boltBindingParam.setTimeout(30000);
        referenceParam.setBindingParam(boltBindingParam);
        referenceParam.setInterfaceType(ProcessService.class);
        referenceParam.setUniqueId(uniqueId);
        return referenceParam;
    }

    private void monitorPort(RpcConfigurationProperties.Bootstrap bootstrap) {
        // 监听端口
        bootstrap.getAppList().forEach(appConfigList -> {
            RpcConfigurationProperties.Bootstrap.AppConfigList.Type type = appConfigList.getType();
            switch (type) {
                case TCP:
                    if (ShortSwitchServer.getInstance(appConfigList.getListenPort()) != null) {
                        logger.error("该端口已被其他应用监听，注册失败！");
                        throw new RuntimeException("Port " + appConfigList.getListenPort() + " already in use");
                    }
                    MinaSocketBean minaSocketBean = AppPropertiesMapper.INSTANCE.convertToMinaSocketBean(appConfigList.getTcpConfig());
                    minaSocketBean.setName(bootstrap.getAppUniqueId());
                    minaSocketBean.setPort(appConfigList.getListenPort());
                    minaSocketBean.setKeepAlive(false);
                    minaSocketBean.setSoLinger(false);
                    try {
                        new ShortSwitchServer().initMinaServer(minaSocketBean);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException | IOException e) {
                        logger.error("启动socket监听失败!", e);
                        throw new RuntimeException(e);
                    }
                    break;
                case HTTP:
                    appProcessFactory.registerUrlList(bootstrap.getAppUniqueId(), appConfigList.getHttpConfig().getUrlList());
                    HttpServer httpServer = HttpServer.getHttpServer(appConfigList.getListenPort());
                    if (httpServer != null) {
                        logger.info("该端口已被其他应用监听，保存HttpServer，无需重复监听");
                        HttpServer.saveHttpServer(bootstrap.getAppUniqueId(), httpServer);
                    } else {
                        new HttpServer().start(bootstrap.getAppUniqueId(), appConfigList.getListenPort(), appConfigList.getHttpConfig());
                    }
                    break;
                default:
                    throw new IllegalArgumentException("参数不合法");
            }
        });
    }

    public ReferenceClient getReferenceClient() {
        return referenceClient;
    }
}
