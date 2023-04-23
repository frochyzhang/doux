package com.allinfinance.dev.rpc.scaffold.processor;

import com.allinfinance.dev.rpc.scaffold.api.dto.bootstrap.AbstractRequestDTO;
import com.allinfinance.dev.rpc.scaffold.api.dto.bootstrap.AbstractResponseDTO;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/16 16:45
 */
public interface BusinessProcessedFactory {
    /**
     * 注册业务处理器
     *
     * @param processor 业务处理器
     */
    void register(BusinessProcessor processor);

    /**
     * 业务处理
     *
     * @param abstractRequestDTO 请求字符串
     * @param processorKey       处理器类型
     * @return 脱敏后字符串
     */
    AbstractResponseDTO processed(AbstractRequestDTO abstractRequestDTO, String processorKey);
}
