package com.allinfinance.dev.boot.socket.autoconfigure;

import com.allinfinance.dev.boot.socket.properties.SocketConfigProperties;
import com.allinfinance.dev.boot.socket.util.SocketPropertyMapper;
import com.allinfinance.dev.core.bean.MinaSocketBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2022/1/26 10:31
 */
@EnableConfigurationProperties(value = SocketConfigProperties.class)
@Configuration
@ComponentScan("com.allinfinance.dev.boot.socket")
public class SocketConfigAutoConfiguration {
    @Autowired
    private SocketConfigProperties socketConfigProperties;

    @Bean(name = "socketBeans")
    public List<MinaSocketBean> buildGlobalSocketBean() {
        List<MinaSocketBean> minaSocketBeanList = socketConfigProperties.getExtConfig()
                .stream().map(SocketPropertyMapper.INSTANCE::convertToMinaSocketBean)
                .collect(Collectors.toList());

        minaSocketBeanList.add(SocketPropertyMapper.INSTANCE.convertToMinaSocketBean(socketConfigProperties));

        return minaSocketBeanList;
    }
}
