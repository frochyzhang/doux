package cn.lezoo.doux.dispatch.scaffold.executor;

import cn.hutool.core.collection.CollectionUtil;
import cn.lezoo.doux.dispatch.scaffold.api.IJobHandler;
import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.handler.impl.MethodJobHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
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
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
@ConfigurationProperties(prefix = "doux.xxl.job.executor")
@Configuration
public class XxlJobCustomExecutor extends XxlJobExecutor {
    private static final Logger logger = LoggerFactory.getLogger(XxlJobCustomExecutor.class);

    private List<IJobHandler> xxlJobBeanList = new ArrayList<>();

    private Integer poolCoreSize;

    private Integer poolMaximumSize;

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
        if (CollectionUtil.isEmpty(xxlJobBeanList)) {
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
                Class<? extends IJobHandler> beanClass = bean.getClass();
                try {
                    executeMethod = beanClass.getDeclaredMethod("execute");
                } catch (NoSuchMethodException e) {
                    executeMethod = beanClass.getSuperclass().getDeclaredMethod("execute");
                }
            } catch (NoSuchMethodException e) {
                logger.error("获取execute方法失败", e);
            }

            // registry jobHandler
            registJobHandler(name, new MethodJobHandler(bean, executeMethod, null, null));
        }
    }
}
