package cn.lezoo.doux.tools.hsp.config;

import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022-12-08 16:54
 */
@Component
@NacosConfigurationProperties(groupId = Constants.DEFAULT_GROUP, type = ConfigType.YAML,
        dataId = "dev-ccp-tool", prefix = "test.network", autoRefreshed = true)
public class CcpNetworkConfig {

    private String socketIp;

    private String socketPort;

    private String name;

    @Override
    public String toString() {
        return "NetworkConfig{" +
                "socketIp='" + socketIp + '\'' +
                ", socketPort='" + socketPort + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getSocketIp() {
        return socketIp;
    }

    public void setSocketIp(String socketIp) {
        this.socketIp = socketIp;
    }

    public String getSocketPort() {
        return socketPort;
    }

    public void setSocketPort(String socketPort) {
        this.socketPort = socketPort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}







