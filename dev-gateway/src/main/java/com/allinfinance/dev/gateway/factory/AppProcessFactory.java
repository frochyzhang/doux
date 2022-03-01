package com.allinfinance.dev.gateway.factory;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.allinfinance.dev.rpc.scaffold.api.ProcessService;
import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/1/28 15:32
 */
@Component
public class AppProcessFactory {
    private static final Map<String, ProcessService> processors = new ConcurrentHashMap<>();
    private static final Map<String, RpcConfigurationProperties.Bootstrap> compares = new ConcurrentHashMap<>();
    private static final Map<String, List<String>> appUrlMap = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(AppProcessFactory.class);

    public void register(String appUniqueId, ProcessService processService) {
        processors.put(appUniqueId, processService);
        compares.put(appUniqueId, processService.init());
    }

    public void register(String appUniqueId, List<String> urls) {
        appUrlMap.put(appUniqueId, urls);
    }

    public Boolean checkIfExist(String appUniqueId, RpcConfigurationProperties.Bootstrap bootstrap) {

        return (processors.containsKey(appUniqueId) &&
                bootstrap.toString().equals(compares.get(appUniqueId).toString()));

    }


    public static String tcpProcessed(String appUniqueId, String requestMsg) {
        return processors.get(appUniqueId).process(requestMsg);
    }

    public static String httpProcessed(String url, String requestMsg) {
        String tmp = url.contains("?") ? url.split("\\?")[0] : url;
        String appUniqueId = appUrlMap.entrySet().stream().filter(entry -> entry.getValue().contains(tmp))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("请求地址" + tmp + "不在应用注册列表内"))
                .getKey();
        return processors.get(appUniqueId).process(requestMsg, url);
    }

    public static List<String> getServiceList(String server) {
        String[] strings = server.split("/");
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, strings[0]);
        properties.put(PropertyKeyConst.NAMESPACE, strings[1]);
        List<String> serviceList = null;
        try {
            NamingService namingService = NacosFactory.createNamingService(properties);
            serviceList = namingService.getServicesOfServer(1, 10).getData()
                    .stream().filter(service -> service.contains(ProcessService.class.getName()))
                    .collect(Collectors.toList());
        } catch (NacosException e) {
            logger.error("NacosException,", e);
        }
        return serviceList;
    }

    public void checkAndProcess(List<String> appList) {
        logger.info("开始检查:{}",appList);
        processors.keySet().stream()
                .filter(app -> !appList.contains(app))
                .forEach(app -> {
                    logger.warn("{}应用已下线，移除网关订阅!", app);
                    processors.remove(app);
                });
    }
}
