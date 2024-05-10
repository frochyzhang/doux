package cn.lezoo.doux.gateway.scaffold.api;

import cn.lezoo.doux.gateway.scaffold.config.Bootstrap;

import java.io.Serializable;

/**
 * @author huanghf
 * @date 2022/11/29 19:51
 */
public class ExporterRegistrarRequest implements Serializable {
    private Bootstrap bootstrap;

    public ExporterRegistrarRequest(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Override
    public String toString() {
        return "GatewayRegistrarRequest{" +
                "bootstrap=" + bootstrap +
                '}';
    }
}
