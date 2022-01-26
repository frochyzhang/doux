package com.allinfinance.dev.actuator;

import com.allinfinance.dev.actuator.model.Greeting;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/1/21 16:47
 */
@RestController
@SpringBootApplication
public class ActuatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(ActuatorApplication.class);
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @GetMapping
    @Timed(description = "耗时")
    @Counted("调用次数")
    public Greeting getGreeting() {
        return new Greeting();
    }

    @GetMapping("/test")
    @Timed(description = "test耗时")
    @Counted("test调用次数")
    public String test() {
        return "alsdjfalskdfjalskdfjlasdjf";
    }
}
