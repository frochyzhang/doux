package cn.lezoo.doux.recoup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2024/1/23 14:03
 */
@EnableFeignClients
@SpringBootApplication
public class RecoupApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecoupApplication.class, args);
    }
}
