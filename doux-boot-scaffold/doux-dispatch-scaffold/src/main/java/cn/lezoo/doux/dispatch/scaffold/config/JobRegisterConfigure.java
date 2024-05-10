package cn.lezoo.doux.dispatch.scaffold.config;

import cn.lezoo.doux.dispatch.scaffold.api.IJobHandler;
import cn.lezoo.doux.dispatch.scaffold.executor.XxlJobCustomExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qipeng
 * @date 2022/1/7 14:52
 */
@Component
public class JobRegisterConfigure implements SmartInitializingSingleton, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(JobRegisterConfigure.class);

    @Autowired
    private XxlJobCustomExecutor xxlJobCustomExecutor;

    private ApplicationContext applicationContext;

    @Override
    public void afterSingletonsInstantiated() {

        List<IJobHandler> iJobHandlers = new ArrayList<>(applicationContext.getBeansOfType(IJobHandler.class).values());
        xxlJobCustomExecutor.setXxlJobBeanList(iJobHandlers);

        // start executor
        try {
            xxlJobCustomExecutor.start();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
