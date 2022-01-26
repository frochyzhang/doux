package com.allinfinance.dev.batch.handler;

import com.allinfinance.dev.dispatch.scaffold.api.IJobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author qipeng
 * @description:
 * @date 2022/1/7 14:32
 */
@Component
public class TestJobHandler implements IJobHandler {
    private static final Logger logger = LoggerFactory.getLogger(TestJobHandler.class);

    /**
     * 设置任务名
     *
     * @return 任务名
     */
    @Override
    public String dispatcherName() {
        return "testHandler";
    }

    @Override
    public void execute() {
        logger.info("=============调用TestJobHandler成功！！！！");
    }

}
