package cn.lezoo.doux.gateway.scaffold.api;

import cn.lezoo.doux.framework.extension.annotation.Extensible;
import cn.lezoo.doux.gateway.scaffold.config.Bootstrap;
import cn.lezoo.doux.gateway.scaffold.processor.dto.ProcessRequestDTO;
import cn.lezoo.doux.gateway.scaffold.processor.dto.ProcessResponseDTO;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/1/28 09:35
 */
@Extensible
public interface ProcessService {
    /**
     * 注册到网关后的验证接口
     *
     * @return
     */
    Boolean verify();

    /**
     * 返回应用参数
     *
     * @return
     */
    Bootstrap init();

    /**
     * 应用系统业务处理服务
     *
     * @param processRequestDTO
     * @return
     */
    ProcessResponseDTO process(ProcessRequestDTO processRequestDTO);
}
