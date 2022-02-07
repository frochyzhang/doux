package com.allinfinance.dev.example.socket.factory;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.LineHandler;
import com.alipay.sofa.rpc.boot.runtime.param.BoltBindingParam;
import com.alipay.sofa.rpc.core.exception.SofaRouteException;
import com.alipay.sofa.runtime.api.aware.ClientFactoryAware;
import com.alipay.sofa.runtime.api.client.ClientFactory;
import com.alipay.sofa.runtime.api.client.ReferenceClient;
import com.alipay.sofa.runtime.api.client.param.BindingParam;
import com.alipay.sofa.runtime.api.client.param.ReferenceParam;
import com.allinfinance.dev.rpc.scaffold.api.ProcessService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;

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

    @Override
    public void setClientFactory(ClientFactory clientFactory) {

        this.referenceClient = clientFactory.getClient(ReferenceClient.class);

        File file = new File(AppProcessFactory.CACHE_DATA_NAME);
        if (file.exists()) {
            FileUtil.readLines(file, StandardCharsets.UTF_8, (LineHandler) s -> {
                AppProcessFactory.AppRegisterProperties appRegisterProperties = new Gson().fromJson(s, AppProcessFactory.AppRegisterProperties.class);
                if (registerConsumer(appRegisterProperties.getUniqueId())) {
                    logger.info("[ {} ]应用注册成功!", appRegisterProperties.getUniqueId());
                }

            });
        }
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
                Integer init = processService.init();
                logger.error("listenPort:{}", init);
                // 监听端口

                // 监听完成

                appProcessFactory.register(uniqueId, processService);
                appProcessFactory.register(new AppProcessFactory.AppRegisterProperties(uniqueId, 8888));
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (SofaRouteException e) {
            logger.warn("[ {} ]已掉线,移除订阅!", uniqueId);
            referenceClient.removeReference(referenceParam);
            return Boolean.FALSE;
        }
    }
}
