package com.allinfinance.dev.example.socket.factory;

import com.allinfinance.dev.rpc.scaffold.api.ProcessService;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/1/28 15:32
 */
@Component
public class AppProcessFactory {
    private static final Map<String, ProcessService> processors = new ConcurrentHashMap<>();

    public static final String CACHE_DATA_PATH = System.getProperty("user.home") + File.separator + ".gate/cache/";
    public static final String CACHE_DATA_NAME = CACHE_DATA_PATH + "cached_data";

    static {
        File file = new File(CACHE_DATA_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void register(String appUniqueId, ProcessService processService) {
        processors.put(appUniqueId, processService);
    }

    public Boolean checkIfExist(String appUniqueId) {
        return processors.containsKey(appUniqueId);
    }


    public String processed(String appUniqueId, String requestMsg) {
        return processors.get(appUniqueId).process(requestMsg);
    }

}
