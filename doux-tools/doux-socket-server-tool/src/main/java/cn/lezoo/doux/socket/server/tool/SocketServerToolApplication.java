package cn.lezoo.doux.socket.server.tool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

/**
 * @author huanghf
 * @date 2023/8/17 9:49
 */
@SpringBootApplication
public class SocketServerToolApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(SocketServerToolApplication.class, args);
        TimeUnit.HOURS.sleep(1L);
    }
}
