package com.allinfinance.dev.boot.socket.autoconfigure;

import com.allinfinance.dev.socket.config.ShortSwitchServer;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2022/1/26 10:31
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({SocketConfigAutoConfiguration.class, ShortSwitchServer.class,
        com.allinfinance.dev.common.socket.server.config.NettyShortSwitchServer.class})
public @interface EnableSocketServer {
}
