package com.allinfinance.dev.gateway.scaffold.api;

import com.allinfinance.dev.common.dictionary.processor.dto.AbstractResponseDTO;

/**
 * @author huanghf
 * @date 2023/12/12 15:02
 */
public class TcpResponseDTO extends AbstractResponseDTO {
    public TcpResponseDTO(String responseMsg) {
        super(responseMsg);
    }

    @Override
    public String toString() {
        return "TcpResponseDTO{} " + super.toString();
    }
}
