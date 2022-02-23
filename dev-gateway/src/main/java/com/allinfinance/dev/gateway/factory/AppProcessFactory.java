package com.allinfinance.dev.gateway.factory;

import com.allinfinance.dev.rpc.scaffold.api.ProcessService;
import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/1/28 15:32
 */
@Component
public class AppProcessFactory {
    private static final Map<String, ProcessService> processors = new ConcurrentHashMap<>();
    private static final Map<String, RpcConfigurationProperties.Bootstrap> compares = new ConcurrentHashMap<>();
    private static final Map<String, List<String>> appUrlMap = new ConcurrentHashMap<>();

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
}
