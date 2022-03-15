package com.allinfinance.dev.gateway.factory;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.allinfinance.dev.gateway.netty.http.NettyHttpRequest;
import com.allinfinance.dev.rpc.scaffold.api.ProcessService;
import com.allinfinance.dev.rpc.scaffold.api.dto.HttpRequestDTO;
import com.allinfinance.dev.rpc.scaffold.api.dto.ProcessRequestDTO;
import com.allinfinance.dev.rpc.scaffold.api.dto.RequestTypeEnum;
import com.allinfinance.dev.rpc.scaffold.api.dto.TcpRequestDTO;
import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        if (appUrlMap.containsKey(appUniqueId)) {
            urls.addAll(appUrlMap.get(appUniqueId));
        }
        appUrlMap.put(appUniqueId, urls);
    }

    public Boolean checkIfExist(String appUniqueId, RpcConfigurationProperties.Bootstrap bootstrap) {

        return (processors.containsKey(appUniqueId) &&
                bootstrap.toString().equals(compares.get(appUniqueId).toString()));

    }


    public static String tcpProcessed(String appUniqueId, String requestMsg) {
        ProcessRequestDTO processRequestDTO = new ProcessRequestDTO(RequestTypeEnum.TCP);
        processRequestDTO.setRequestDTO(new TcpRequestDTO(requestMsg));
        return processors.get(appUniqueId).process(processRequestDTO);
    }

    private static final Pattern PATTERN = Pattern.compile("\\t|\r|\n");

    public static String httpProcessed(NettyHttpRequest request) {
        String urlWithParam = request.getUri();
        String requestMsg = request.contentText();

        String url = urlWithParam.contains("?") ? urlWithParam.split("\\?")[0] : urlWithParam;
        String paramString = urlWithParam.contains("?") ? urlWithParam.split("\\?")[1] : "";

        String appUniqueId = appUrlMap.entrySet().stream().filter(entry -> entry.getValue().contains(url))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("请求地址" + url + "不在应用注册列表内"))
                .getKey();

        ProcessRequestDTO processRequestDTO = new ProcessRequestDTO(RequestTypeEnum.HTTP);
        HttpRequestDTO httpRequestDTO = new HttpRequestDTO(null);
        httpRequestDTO.setUrl(url);


        Map<String, String> params = StringUtils.isNotBlank(paramString) ? Arrays.stream(paramString.split("&"))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1])) : new HashMap<>(0);

        Map<String, String> headers = request.headers().entries().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        httpRequestDTO.setParams(params);
        httpRequestDTO.setHeaders(headers);
        httpRequestDTO.setHttpMethod(HttpMethod.valueOf(request.method().name()));

        if (StringUtils.isNotBlank(requestMsg)) {
            Matcher m = PATTERN.matcher(requestMsg);
            httpRequestDTO.setRequestMsg(m.replaceAll(""));
        }

        processRequestDTO.setRequestDTO(httpRequestDTO);
        return processors.get(appUniqueId).process(processRequestDTO);
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
        logger.info("开始检查:{}", appList);
        processors.keySet().stream()
                .filter(app -> !appList.contains(app))
                .forEach(app -> {
                    logger.warn("{}应用已下线，移除网关订阅!", app);
                    processors.remove(app);
                });
    }
}
