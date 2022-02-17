package com.allinfinance.dev.gateway.socket.factory;

import com.allinfinance.dev.rpc.scaffold.api.ProcessService;
import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;
import org.springframework.stereotype.Component;

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

    public void register(String appUniqueId, ProcessService processService) {
        processors.put(appUniqueId, processService);
        compares.put(appUniqueId, processService.init());
    }

    public Boolean checkIfExist(String appUniqueId, RpcConfigurationProperties.Bootstrap bootstrap) {

        return (processors.containsKey(appUniqueId) &&
                bootstrap.toString().equals(compares.get(appUniqueId).toString()));

    }


    public static String processed(String appUniqueId, String requestMsg) {
        return processors.get(appUniqueId).process(requestMsg);
    }

}
