package com.allinfinance.dev.gateway.factory;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.alipay.sofa.rpc.boot.runtime.param.BoltBindingParam;
import com.alipay.sofa.runtime.api.client.param.ReferenceParam;
import com.allinfinance.dev.gateway.netty.HttpServer;
import com.allinfinance.dev.gateway.netty.http.NettyHttpRequest;
import com.allinfinance.dev.rpc.scaffold.api.ProcessService;
import com.allinfinance.dev.rpc.scaffold.api.dto.HttpRequestDTO;
import com.allinfinance.dev.rpc.scaffold.api.dto.HttpResponseDTO;
import com.allinfinance.dev.rpc.scaffold.api.dto.ProcessRequestDTO;
import com.allinfinance.dev.rpc.scaffold.api.dto.RequestTypeEnum;
import com.allinfinance.dev.rpc.scaffold.api.dto.TcpRequestDTO;
import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;
import com.allinfinance.dev.socket.config.ShortSwitchServer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private static final Map<String, List<RpcConfigurationProperties.Bootstrap.AppConfigList.HttpConfig.UrlConfig>> appUrlMap = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(AppProcessFactory.class);

    @Autowired
    private GateClientFactoryAware gateClientFactoryAware;

    public void registerProcessService(String appUniqueId, ProcessService processService) {
        processors.put(appUniqueId, processService);
        compares.put(appUniqueId, processService.init());
    }

    public void registerUrlList(String appUniqueId, List<RpcConfigurationProperties.Bootstrap.AppConfigList.HttpConfig.UrlConfig> urls) {
        if (appUrlMap.containsKey(appUniqueId)) {
            urls.addAll(appUrlMap.get(appUniqueId));
        }
        appUrlMap.put(appUniqueId, urls);
    }

    public void registerBootstrap(RpcConfigurationProperties.Bootstrap bootstrap) {
        compares.put(bootstrap.getAppUniqueId(), bootstrap);
    }

    public Boolean checkIfExist(String appUniqueId, RpcConfigurationProperties.Bootstrap bootstrap) {

        return (processors.containsKey(appUniqueId) &&
                bootstrap.toString().equals(compares.get(appUniqueId).toString()));

    }

    public boolean checkProcessServiceIfExist(String appUniqueId) {
        return processors.containsKey(appUniqueId);
    }

    public boolean checkBootstrapIfEqual(RpcConfigurationProperties.Bootstrap bootstrap) {
        return bootstrap.toString().equals(compares.get(bootstrap.getAppUniqueId()).toString());
    }

    public static String tcpProcessed(String appUniqueId, String requestMsg) {
        ProcessRequestDTO processRequestDTO = new ProcessRequestDTO(RequestTypeEnum.TCP);
        processRequestDTO.setRequestDTO(new TcpRequestDTO(requestMsg));
        return processors.get(appUniqueId).process(processRequestDTO).getResponseDTO().getResponseMsg();
    }

    public static HttpResponseDTO httpProcessed(String appUniqueId, NettyHttpRequest request, int port) {
        String urlWithParam = request.getUri();
        String requestMsg = request.contentText();

        String url = urlWithParam.contains("?") ? urlWithParam.split("\\?")[0] : urlWithParam;
        String paramString = urlWithParam.contains("?") ? urlWithParam.split("\\?")[1] : "";

        //装配ProcessRequestDTO
        ProcessRequestDTO processRequestDTO = new ProcessRequestDTO(RequestTypeEnum.HTTP);
        HttpRequestDTO httpRequestDTO = new HttpRequestDTO(null);
        httpRequestDTO.setUrl(url);
        //取出url中的params
        Map<String, String> params = StringUtils.isNotBlank(paramString) ? Arrays.stream(paramString.split("&"))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(arr -> arr[0], arr -> arr.length > 1 ? arr[1] : "")) : new HashMap<>(0);
        httpRequestDTO.setParams(params);
        //取出header中的信息
        Map<String, String> headers = request.headers().entries().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        httpRequestDTO.setHeaders(headers);
        httpRequestDTO.setHttpMethod(HttpMethod.valueOf(request.method().name()));
        httpRequestDTO.setRequestMsg(requestMsg);
        processRequestDTO.setRequestDTO(httpRequestDTO);

        //1、根据appUniqueId查找urlList，判断是否包含url，如果包含则直接调用
        List<String> urlList = appUrlMap.get(appUniqueId)
                .stream()
                .map(RpcConfigurationProperties.Bootstrap.AppConfigList.HttpConfig.UrlConfig::getUrl)
                .collect(Collectors.toList());
        if (!urlList.contains(url)) {
            //2、不包含url则去寻找其他监听了这个端口的应用
            String finalAppUniqueId = appUniqueId;
            List<RpcConfigurationProperties.Bootstrap> bootstrapList = compares.values()
                    .stream()
                    .filter(bootstrap -> !finalAppUniqueId.equals(bootstrap.getAppUniqueId()) && bootstrap.getAppList().stream()
                            .anyMatch(appConfigList -> appConfigList.getListenPort().equals(port)))
                    .collect(Collectors.toList());
            //3、遍历监听了这个端口的应用列表，判断是否包含url，如果包含则直接调用
            appUniqueId = bootstrapList.stream()
                    .filter(bootstrap -> appUrlMap.get(bootstrap.getAppUniqueId())
                            .stream()
                            .map(RpcConfigurationProperties.Bootstrap.AppConfigList.HttpConfig.UrlConfig::getUrl)
                            .collect(Collectors.toList())
                            .contains(url))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("请求地址" + url + "不在应用注册列表内"))
                    .getAppUniqueId();
        }
        ProcessService processService = processors.get(appUniqueId);
        if (processService == null) {
            logger.error("未找到对应http请求处理服务, appUniqueId: {}", appUniqueId);
            throw new IllegalArgumentException("调用http请求处理服务失败");
        }
        //判断请求类型是否与配置的一致
        HttpMethod requestMethod = appUrlMap.get(appUniqueId)
                .stream()
                .filter(urlConfig -> url.equals(urlConfig.getUrl()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("请求地址" + url + "不在应用注册列表内"))
                .getRequestMethod();
        if (requestMethod.equals(HttpMethod.valueOf(request.method().name()))) {
            return ((HttpResponseDTO) processService.process(processRequestDTO).getResponseDTO());
        } else {
            throw new IllegalArgumentException("不合法的请求类型: " + request.method());
        }
    }

    public static List<String> getServiceList(String server) {
        String[] strings = server.split("/");
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, strings[0]);
        properties.put(PropertyKeyConst.NAMESPACE, strings[1]);
        List<String> serviceList = new ArrayList<>();
        try {
            NamingService namingService = NacosFactory.createNamingService(properties);
            int i = 1;
            while (true) {
                ListView<String> servicesOfServer = namingService.getServicesOfServer(i, 10);
                i++;
                if (servicesOfServer.getData().size() > 0) {
                    serviceList.addAll(servicesOfServer.getData());
                } else {
                    break;
                }
            }
            serviceList = serviceList.stream()
                    .filter(service -> service.contains(ProcessService.class.getName()))
                    .collect(Collectors.toList());
        } catch (NacosException e) {
            logger.error("NacosException,", e);
        }
        return serviceList;
    }

    public void removeBootstrap(String uniqueId) {
        logger.info("开始移除[ {} ]应用配置", uniqueId);
        RpcConfigurationProperties.Bootstrap removeBootstrap = compares.remove(uniqueId);
        logger.info("旧的配置项: {}", removeBootstrap);
        List<RpcConfigurationProperties.Bootstrap.AppConfigList.HttpConfig.UrlConfig> removeUrlConfigList = appUrlMap.remove(uniqueId);
        logger.info("旧的URL列表信息: {}", removeUrlConfigList);
    }

    public void removePortMonitor(String appUniqueId) {
        //移除http端口监听
        Optional.ofNullable(HttpServer.getInstance(appUniqueId))
                .ifPresent(httpServerList -> {
                    List<HttpServer> toBeClosedHttpServerList = httpServerList
                            .stream()
                            .filter(httpServer -> {
                                //是否有其它监听这个端口的应用
                                List<RpcConfigurationProperties.Bootstrap> bootstrapList = compares.values()
                                        .stream()
                                        .filter(bootstrap -> !appUniqueId.equals(bootstrap.getAppUniqueId())
                                                && bootstrap.getAppList().stream()
                                                .anyMatch(appConfigList -> appConfigList.getListenPort().equals(httpServer.getPort())))
                                        .collect(Collectors.toList());
                                return CollectionUtils.isEmpty(bootstrapList);
                            }).collect(Collectors.toList());
                    //切忌直接在流中修改collection结构
                    toBeClosedHttpServerList.forEach(httpServer -> httpServer.shutdown(appUniqueId));
                });
        //移除tcp端口监听
        Optional.ofNullable(compares.get(appUniqueId))
                .ifPresent(bootstrap -> bootstrap.getAppList()
                        .stream()
                        .filter(appConfigList -> RpcConfigurationProperties.Bootstrap.AppConfigList.Type.TCP.equals(appConfigList.getType()))
                        .forEach(appConfigList -> ShortSwitchServer.closeMinaServer(appConfigList.getListenPort())));
    }

    public void removeReference(ReferenceParam<ProcessService> referenceParam) {
        String uniqueId = referenceParam.getUniqueId();
        logger.warn("准备移除[appUniqueId:{}, interfaceType:{}]订阅", uniqueId, referenceParam.getInterfaceType());

        ProcessService removeRet = processors.remove(uniqueId);
        if (removeRet != null) {
            logger.warn("网关缓存应用[{}]的ProcessService已移除，准备移除sofa订阅", uniqueId);
            gateClientFactoryAware.getReferenceClient().removeReference(referenceParam);
        } else {
            logger.info("未缓存应用[{}]的ProcessService服务，无需移除sofa订阅", uniqueId);
        }
    }

    public void removeAll(String uniqueId) {
        //移除端口监听
        removePortMonitor(uniqueId);
        //移除网关订阅
        ReferenceParam<ProcessService> referenceParam = new ReferenceParam<>();
        BoltBindingParam boltBindingParam = new BoltBindingParam();
        boltBindingParam.setLoadBalancer("roundRobin");
        referenceParam.setBindingParam(boltBindingParam);
        referenceParam.setInterfaceType(ProcessService.class);
        referenceParam.setUniqueId(uniqueId);
        removeReference(referenceParam);
        //移除配置信息，包括compares和appUrlMap
        removeBootstrap(uniqueId);
    }

    public Map<String, ProcessService> getProcessors() {
        return processors;
    }

    public Map<String, RpcConfigurationProperties.Bootstrap> getCompares() {
        return compares;
    }

    public Map<String, List<RpcConfigurationProperties.Bootstrap.AppConfigList.HttpConfig.UrlConfig>> getAppUrlMap() {
        return appUrlMap;
    }
}
