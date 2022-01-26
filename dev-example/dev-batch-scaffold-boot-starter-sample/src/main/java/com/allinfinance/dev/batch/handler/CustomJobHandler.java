package com.allinfinance.dev.batch.handler;

import com.allinfinance.dev.dispatch.scaffold.api.IJobHandler;
import com.xxl.job.core.context.XxlJobHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author qipeng
 * @description:
 * @date 2022/1/20 17:20
 */
@Component
public class CustomJobHandler implements IJobHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomJobHandler.class);

    /**
     * 设置任务名
     *
     * @return 任务名
     */
    @Override
    public String dispatcherName() {
        return "customJobHandler";
    }

    /**
     * 任务执行方法
     */
    @Override
    public void execute() {
        String param = XxlJobHelper.getJobParam();
        logger.info("=============调用CustomJobHandler成功：\\n" + param);
    }
}
