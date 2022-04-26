package com.allinfinance.dev.gateway.factory;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alipay.sofa.runtime.api.client.param.ReferenceParam;
import com.allinfinance.dev.gateway.netty.http.NettyHttpRequest;
import com.allinfinance.dev.rpc.scaffold.api.ProcessService;
import com.allinfinance.dev.rpc.scaffold.api.dto.*;
import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.*;
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
    private static final Map<String, List<RpcConfigurationProperties.Bootstrap.AppConfigList.HttpConfig.UrlConfig>> appUrlMap = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(AppProcessFactory.class);

    @Autowired
    private GateClientFactoryAware gateClientFactoryAware;

    public void register(String appUniqueId, ProcessService processService) {
        processors.put(appUniqueId, processService);
        compares.put(appUniqueId, processService.init());
    }

    public void register(String appUniqueId, List<RpcConfigurationProperties.Bootstrap.AppConfigList.HttpConfig.UrlConfig> urls) {
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
        return processors.get(appUniqueId).process(processRequestDTO).getResponseDTO().getResponseMsg();
    }

    private static final Pattern PATTERN = Pattern.compile("\\t|\r|\n");

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
                .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1])) : new HashMap<>(0);
        httpRequestDTO.setParams(params);
        //取出header中的信息
        Map<String, String> headers = request.headers().entries().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        httpRequestDTO.setHeaders(headers);
        httpRequestDTO.setHttpMethod(HttpMethod.valueOf(request.method().name()));
        //对请求体里的空行和多余的空格进行处理
        if (StringUtils.isNotBlank(requestMsg)) {
            Matcher m = PATTERN.matcher(requestMsg);
            httpRequestDTO.setRequestMsg(m.replaceAll(""));
        }
        processRequestDTO.setRequestDTO(httpRequestDTO);

        //1、根据appUniqueId查找urlList，判断是否包含url，如果包含则直接调用
        List<RpcConfigurationProperties.Bootstrap.AppConfigList.HttpConfig.UrlConfig> urlConfigList = appUrlMap.get(appUniqueId);
        List<String> urlList = appUrlMap.get(appUniqueId)
                .stream()
                .map(RpcConfigurationProperties.Bootstrap.AppConfigList.HttpConfig.UrlConfig::getUrl)
                .collect(Collectors.toList());
        if (!urlList.contains(url)) {
            //2、不包含url则去寻找其他监听了这个端口的应用
            List<RpcConfigurationProperties.Bootstrap> bootstrapList = compares.values()
                    .stream()
                    .filter(bootstrap -> bootstrap.getAppList().stream()
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
            return ((HttpResponseDTO) processors.get(appUniqueId).process(processRequestDTO).getResponseDTO());
        } else {
            throw new IllegalArgumentException("不合法的请求类型: " + request.method());
        }
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

    public void removeReference(ReferenceParam<ProcessService> referenceParam) {
        String uniqueId = referenceParam.getUniqueId();
        logger.warn("准备移除[appUniqueId:{}, interfaceType:{}]订阅", uniqueId, referenceParam.getInterfaceType());

        ProcessService removeRet = processors.remove(uniqueId);
        if (removeRet != null) {
            logger.warn("appUniqueId:{}网关缓冲池已移除，准备移除sofa订阅", uniqueId);
            gateClientFactoryAware.getReferenceClient().removeReference(referenceParam);
        } else {
            logger.info("应用[{}]不存在", uniqueId);
        }
    }
}
