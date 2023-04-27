package com.allinfinance.dev.common.dictionary.processor.api;

import com.allinfinance.dev.common.dictionary.processor.dto.AbstractRequestDTO;
import com.allinfinance.dev.common.dictionary.processor.dto.AbstractResponseDTO;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/16 16:46
 */
public interface BusinessProcessor {
    String processorKey();

    AbstractResponseDTO process(AbstractRequestDTO abstractRequestDTO);
}
