package cn.lezoo.doux.gateway.scaffold.api;

import cn.lezoo.doux.gateway.scaffold.processor.dto.AbstractResponseDTO;

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
