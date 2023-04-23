package com.allinfinance.dev.rpc.scaffold.api.dto.bootstrap;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2022/3/15 9:45
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
