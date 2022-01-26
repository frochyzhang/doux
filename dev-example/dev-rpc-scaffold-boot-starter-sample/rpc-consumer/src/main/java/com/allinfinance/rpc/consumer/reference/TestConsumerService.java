package com.allinfinance.rpc.consumer.reference;

import com.allinfinance.rpc.scaffold.api.TestApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2021/12/30 22:35
 */
@Service
public class TestConsumerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestConsumerService.class);

    @Autowired
    private TestApi testApi;

    public void test() {
        LOGGER.info(testApi.message());
    }
}
