package com.allinfinance.dev.rpc.scaffold.api.dto.bootstrap;

import com.allinfinance.dev.common.dictionary.processor.dto.AbstractResponseDTO;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2022/3/15 9:45
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
