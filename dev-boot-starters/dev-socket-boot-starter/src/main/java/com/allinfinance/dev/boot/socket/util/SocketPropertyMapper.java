package com.allinfinance.dev.boot.socket.util;

import com.allinfinance.dev.boot.socket.properties.SocketConfigPropertie;
import com.allinfinance.dev.boot.socket.properties.SocketConfigProperties;
import com.allinfinance.dev.common.socket.server.Bean.NettySocketBean;
import com.allinfinance.dev.core.bean.MinaSocketBean;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2022/1/26 10:33
 */
@Mapper
public interface SocketPropertyMapper {

    SocketPropertyMapper INSTANCE = Mappers.getMapper(SocketPropertyMapper.class);

    @Mappings({
            @Mapping(source = "keepAlive.enable", target = "keepAlive"),
            @Mapping(source = "keepAlive.beatTimeout", target = "beatTimeout"),
            @Mapping(source = "keepAlive.beatInterval", target = "beatInterval"),
    })
    MinaSocketBean convertToMinaSocketBean(SocketConfigProperties socketConfigProperties);

    @Mappings({
            @Mapping(source = "keepAlive.enable", target = "keepAlive"),
            @Mapping(source = "keepAlive.beatTimeout", target = "beatTimeout"),
            @Mapping(source = "keepAlive.beatInterval", target = "beatInterval"),
    })
    MinaSocketBean convertToMinaSocketBean(SocketConfigProperties.Config extConfig);

    //netty部分
    @Mappings({
            @Mapping(source = "keepAlive.enable", target = "keepAlive"),
            @Mapping(source = "keepAlive.beatTimeout", target = "beatTimeout"),
            @Mapping(source = "keepAlive.beatInterval", target = "beatInterval"),
    })
    NettySocketBean convertToNettySocketBean(SocketConfigPropertie socketConfigPropertie);

    @Mappings({
            @Mapping(source = "keepAlive.enable", target = "keepAlive"),
            @Mapping(source = "keepAlive.beatTimeout", target = "beatTimeout"),
            @Mapping(source = "keepAlive.beatInterval", target = "beatInterval"),
    })
    NettySocketBean convertToNettySocketBean(SocketConfigPropertie.Config extConfig);
}
