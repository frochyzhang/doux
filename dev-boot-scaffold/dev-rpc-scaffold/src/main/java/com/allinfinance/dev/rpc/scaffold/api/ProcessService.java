package com.allinfinance.dev.rpc.scaffold.api;

import com.allinfinance.dev.rpc.scaffold.api.dto.ProcessRequestDTO;
import com.allinfinance.dev.rpc.scaffold.api.dto.ProcessResponseDTO;
import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/1/28 09:35
 */
public interface ProcessService {
    /**
     * 注册到网关后的验证接口
     *
     * @return
     */
    Boolean verify();

    /**
     * 返回应用参数
     *
     * @return
     */
    RpcConfigurationProperties.Bootstrap init();

    /**
     * 应用系统业务处理服务
     *
     * @param processRequestDTO
     * @return
     */
    ProcessResponseDTO process(ProcessRequestDTO processRequestDTO);
}
