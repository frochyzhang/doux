package cn.lezoo.doux.recoup.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2024/1/23 16:17
 */
@ConfigurationPropertiesScan
@EnableJpaRepositories
@SpringBootApplication
public class RecoupServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecoupServerApplication.class, args);
    }
}
