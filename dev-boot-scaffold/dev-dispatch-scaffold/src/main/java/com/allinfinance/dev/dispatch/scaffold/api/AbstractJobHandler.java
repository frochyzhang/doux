package com.allinfinance.dev.dispatch.scaffold.api;

import com.allinfinance.dev.dispatch.scaffold.executor.XxlJobCustomExecutor;
import com.xxl.job.core.context.XxlJobHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.*;

/**
 * @author qipeng
 * @date 2022/11/8 11:06
 * @desc
 */
public abstract class AbstractJobHandler implements IJobHandler, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(AbstractJobHandler.class);

    @Autowired
    private XxlJobCustomExecutor xxlJobCustomExecutor;

    private static ExecutorService executor;


    public void wrappedExecute() {
        executor.execute(() -> {
            try {
                this.execute();
                XxlJobHelper.log("任务调度成功！");
            } catch (Exception e) {
                logger.error("任务调度异常", e);
                XxlJobHelper.log("任务调度异常，待重新拉起批量！");
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ThreadFactory threadFactory = new cn.hutool.core.thread.ThreadFactoryBuilder()
                .setNamePrefix("executor-thread-")
                .setDaemon(true)
                .build();
        if (executor == null) {
            executor = new ThreadPoolExecutor(xxlJobCustomExecutor.getPoolCoreSize(), xxlJobCustomExecutor.getPoolMaximumSize(), 0L,
                    TimeUnit.MICROSECONDS, new SynchronousQueue<>(), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
        }
    }
}
