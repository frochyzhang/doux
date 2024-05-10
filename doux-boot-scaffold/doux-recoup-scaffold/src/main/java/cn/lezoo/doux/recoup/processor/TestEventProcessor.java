package cn.lezoo.doux.recoup.processor;

import cn.lezoo.doux.disruptor.processor.AbstractEventProcessor;
import cn.lezoo.doux.recoup.client.RegistryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2024/1/23 14:11
 */
@Component
public class TestEventProcessor extends AbstractEventProcessor<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestEventProcessor.class);

    private final RegistryClient registryClient;

    public TestEventProcessor(RegistryClient registryClient) {
        this.registryClient = registryClient;
    }

    @Override
    public String processKey() {
        return "test";
    }

    @Override
    public void process(Object data) {
//        String className = data.getClass().getName();
//        String localhostStr = NetUtil.getLocalhostStr();
//        String appName = SpringUtil.getProperty("spring.application.name");
//        LOGGER.info("className:{},localhostStr:{},appName:{}", className, localhostStr, appName);
//        LOGGER.info("data:{}", data);
//        registryClient.registry(MetaRegistryCmd.builder()
//            .appName(appName)
//            .host(localhostStr)
//            .port(8888)
//            .metaData(MetaRegistryCmd.MetaData.builder()
//                .className(className)
//                .data(JSONUtil.toJsonStr(data))
//                .build())
//            .build());
        // done
    }
}
