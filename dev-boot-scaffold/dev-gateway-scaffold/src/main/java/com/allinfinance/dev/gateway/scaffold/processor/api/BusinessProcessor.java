package com.allinfinance.dev.gateway.scaffold.processor.api;

import com.allinfinance.dev.gateway.scaffold.processor.dto.AbstractRequestDTO;
import com.allinfinance.dev.gateway.scaffold.processor.dto.AbstractResponseDTO;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/16 16:46
 */
public interface BusinessProcessor {
    String processorKey();

    AbstractResponseDTO process(AbstractRequestDTO abstractRequestDTO);
}
