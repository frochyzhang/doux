package com.allinfinance.dev.core.util.hsp;

import com.allinfinance.dev.core.dto.hsp.SummaryGetRequestDTO;
import com.allinfinance.dev.core.dto.hsp.SummaryGetResponseDTO;

/**
 * @author huanghf
 * @date 2022/6/21 14:23
 */
public interface ISummaryService {
    /**
     * 获取摘要
     *
     * @param summaryGetRequestDTO 摘要参数
     * @return 摘要
     */
    SummaryGetResponseDTO getSummary(SummaryGetRequestDTO summaryGetRequestDTO);
}
