package com.allinfinance.dev.example.socket.factory;

import com.alipay.sofa.rpc.boot.runtime.param.BoltBindingParam;
import com.alipay.sofa.rpc.core.exception.SofaRouteException;
import com.alipay.sofa.runtime.api.aware.ClientFactoryAware;
import com.alipay.sofa.runtime.api.client.ClientFactory;
import com.alipay.sofa.runtime.api.client.ReferenceClient;
import com.alipay.sofa.runtime.api.client.param.BindingParam;
import com.alipay.sofa.runtime.api.client.param.ReferenceParam;
import com.allinfinance.dev.rpc.scaffold.api.ProcessService;
import com.google.gson.Gson;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/1/29 09:37
 */
@Service
public class GateClientFactoryAware implements ClientFactoryAware {
    private static final Logger logger = LoggerFactory.getLogger(GateClientFactoryAware.class);

    @Autowired
    private AppProcessFactory appProcessFactory;

//    private ClientFactory clientFactory;

    private ReferenceClient referenceClient;

    @Override
    public void setClientFactory(ClientFactory clientFactory) {

//        this.clientFactory = clientFactory;
        this.referenceClient = clientFactory.getClient(ReferenceClient.class);


        String url = "http://10.100.79.102:8848/nacos/v1/ns/service/list?pageNo=1&pageSize=10&serviceNameParam=&groupNameParam=&namespaceId=iasp-zy";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                logger.error("onFailure: {}", call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body().string();
                ServiceListDTO serviceListDTO = new Gson().fromJson(resp, ServiceListDTO.class);
                serviceListDTO.getDoms().stream()
                        .filter(service -> service.contains(ProcessService.class.getName()))
                        .forEach(service -> {
                            String uniqueId = service.split(":")[1];
                            if (registerConsumer(uniqueId)) {
                                logger.info("[ {} ]应用注册成功!", uniqueId);
                            }
                        });
            }
        });


    }

    public Boolean registerConsumer(String uniqueId) {
        if (appProcessFactory.checkIfExist(uniqueId)) {
            logger.info("[ {} ]无需重复注册", uniqueId);
            return Boolean.TRUE;
        }

        ReferenceParam<ProcessService> referenceParam = new ReferenceParam<>();
        BindingParam refBindingParam = new BoltBindingParam();
        referenceParam.setBindingParam(refBindingParam);

        referenceParam.setInterfaceType(ProcessService.class);
        referenceParam.setUniqueId(uniqueId);
        ProcessService processService = referenceClient.reference(referenceParam);
        try {
            if (processService.verify()) {
                logger.info("[ {} ]业务处理服务订阅成功!", uniqueId);
                // 监听端口

                // 监听完成

                appProcessFactory.register(uniqueId, processService);
                AppProcessFactory.PROCESSOR_PROPERTIES_MAP.put(uniqueId, new AppProcessFactory.ProcessorProperties(uniqueId, 8888));
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (SofaRouteException e) {
            logger.warn("[ {} ]已掉线,移除订阅!", uniqueId);
            referenceClient.removeReference(referenceParam);
            return Boolean.FALSE;
        }
    }

    public static class ServiceListDTO {
        private List<String> doms;
        private Integer count;

        public List<String> getDoms() {
            return doms;
        }

        public void setDoms(List<String> doms) {
            this.doms = doms;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "ServiceListDTO{" +
                    "doms=" + doms +
                    ", count=" + count +
                    '}';
        }
    }
}
