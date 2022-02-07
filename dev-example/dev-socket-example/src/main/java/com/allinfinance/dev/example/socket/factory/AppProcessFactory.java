package com.allinfinance.dev.example.socket.factory;

import cn.hutool.core.io.FileUtil;
import com.allinfinance.dev.rpc.scaffold.api.ProcessService;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    private static final List<AppRegisterProperties> APP_REGISTER_PROPERTIES = new ArrayList<>();

    public static final String CACHE_DATA_PATH = System.getProperty("user.home") + File.separator + ".gate/cache/";
    public static final String CACHE_DATA_NAME = CACHE_DATA_PATH + "cached_data";

    static {
        File file = new File(CACHE_DATA_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void register(AppRegisterProperties appRegisterProperties) {
        APP_REGISTER_PROPERTIES.add(appRegisterProperties);
    }

    public void register(String appUniqueId, ProcessService processService) {
        processors.put(appUniqueId, processService);
    }

    public void cacheProcessors() {
        FileUtil.writeLines(APP_REGISTER_PROPERTIES, CACHE_DATA_NAME, StandardCharsets.UTF_8);
    }

    public Boolean checkIfExist(String appUniqueId) {
        return processors.containsKey(appUniqueId);
    }


    public String processed(String appUniqueId, String requestMsg) {
        return processors.get(appUniqueId).process(requestMsg);
    }

    public static class AppRegisterProperties {
        private String uniqueId;
        private Integer port;

        public AppRegisterProperties(String uniqueId, Integer port) {
            this.uniqueId = uniqueId;
            this.port = port;
        }

        public String getUniqueId() {
            return uniqueId;
        }

        public Integer getPort() {
            return port;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}
