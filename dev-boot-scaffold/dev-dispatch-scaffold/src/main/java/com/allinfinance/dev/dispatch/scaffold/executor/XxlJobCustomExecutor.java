package com.allinfinance.dev.dispatch.scaffold.executor;

import com.allinfinance.dev.dispatch.scaffold.api.IJobHandler;
import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.handler.impl.MethodJobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * @author qipeng
 * @date 2022/1/20 11:38
 */
@ConfigurationProperties(prefix = "com.allinfinance.xxl.job.executor")
@Configuration
public class XxlJobCustomExecutor extends XxlJobExecutor {
    private static final Logger logger = LoggerFactory.getLogger(XxlJobCustomExecutor.class);

    private List<IJobHandler> xxlJobBeanList = new ArrayList<>();

    @Override
    public void start() {
        // init JobHandler Repository (for method)
        initJobHandlerMethodRepository(xxlJobBeanList);

        // super start
        try {
            super.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }


    private void initJobHandlerMethodRepository(List<IJobHandler> xxlJobBeanList) {
        if (xxlJobBeanList == null || xxlJobBeanList.size() == 0) {
            logger.info("缺少JobHandler！");
            return;
        }

        // init job handler from method
        for (IJobHandler bean : xxlJobBeanList) {
            String name = bean.dispatcherName();

            if (loadJobHandler(name) != null) {
                throw new RuntimeException("xxl-job jobHandler[" + name + "] naming conflicts.");
            }

            Method executeMethod = null;
            try {
                executeMethod = bean.getClass().getDeclaredMethod("execute");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            // registry jobHandler
            registJobHandler(name, new MethodJobHandler(bean, executeMethod, null, null));
        }
    }

    public List<IJobHandler> getXxlJobBeanList() {
        return xxlJobBeanList;
    }

    public void setXxlJobBeanList(List<IJobHandler> xxlJobBeanList) {
        this.xxlJobBeanList = xxlJobBeanList;
    }
}