package com.allinfinance.dev.rpc.scaffold.processor;

import com.allinfinance.dev.rpc.scaffold.api.dto.bootstrap.AbstractRequestDTO;
import com.allinfinance.dev.rpc.scaffold.api.dto.bootstrap.AbstractResponseDTO;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/16 16:46
 */
public interface BusinessProcessor {
    String processorKey();

    AbstractResponseDTO process(AbstractRequestDTO abstractRequestDTO);
}
