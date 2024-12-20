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
    public static void main(String[] args) {
        SpringApplication.run(SocketServerToolApplication.class, args);
        while (true) {
            try {
                TimeUnit.HOURS.sleep(1L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
