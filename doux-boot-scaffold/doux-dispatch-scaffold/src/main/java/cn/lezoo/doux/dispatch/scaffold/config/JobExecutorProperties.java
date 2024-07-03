package cn.lezoo.doux.dispatch.scaffold.config;

import cn.lezoo.doux.dispatch.scaffold.executor.XxlJobCustomExecutor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author qipeng
 * @description: 获取xxl-job执行器相关配置
 * @date 2022/1/7 16:12
 */
@Data
@Accessors(chain = true)
@Configuration
@ConfigurationProperties(prefix = "doux.xxl.job")
public class JobExecutorProperties {
    private String adminAddresses;
    private String appName;
    @Autowired
    private XxlJobCustomExecutor xxlJobCustomExecutor;

    @PostConstruct
    private void initXxlJobExecutor() {
        xxlJobCustomExecutor.setAppname(appName);
        xxlJobCustomExecutor.setAdminAddresses(adminAddresses);
    }
}
