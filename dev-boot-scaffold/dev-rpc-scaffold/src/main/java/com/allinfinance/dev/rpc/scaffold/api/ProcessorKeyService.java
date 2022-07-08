package com.allinfinance.dev.rpc.scaffold.api;

import com.allinfinance.dev.rpc.scaffold.api.dto.ProcessRequestDTO;

/**
 * @author huanghf
 * @date 2022/7/5 15:46
 */
public interface ProcessorKeyService {
    /**
     * 通过网关ProcessRequestDTO获取processorKey
     *
     * @param processRequestDTO 网关业务处理请求DTO
     * @return processorKey
     */
    String getProcessorKey(ProcessRequestDTO processRequestDTO);
}
