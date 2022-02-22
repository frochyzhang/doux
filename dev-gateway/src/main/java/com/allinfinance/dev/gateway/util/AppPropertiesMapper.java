package com.allinfinance.dev.gateway.util;

import com.allinfinance.dev.core.bean.MinaSocketBean;
import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author zhangrn
 * @date 2022/2/8
 */
@Mapper
public interface AppPropertiesMapper {

    AppPropertiesMapper INSTANCE = Mappers.getMapper(AppPropertiesMapper.class);

    MinaSocketBean convertToMinaSocketBean(RpcConfigurationProperties.Bootstrap.AppConfigList.TcpConfig tcpConfig);
}
