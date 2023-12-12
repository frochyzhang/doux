package com.allinfinance.dev.gateway.scaffold.api;

import com.allinfinance.dev.common.dictionary.processor.dto.AbstractRequestDTO;

/**
 * @author huanghf
 * @date 2023/12/12 15:01
 */
public class TcpRequestDTO extends AbstractRequestDTO {
    public TcpRequestDTO(String requestMsg) {
        super(requestMsg);
    }

    @Override
    public String toString() {
        return "TcpRequestDTO{} " + super.toString();
    }
}
