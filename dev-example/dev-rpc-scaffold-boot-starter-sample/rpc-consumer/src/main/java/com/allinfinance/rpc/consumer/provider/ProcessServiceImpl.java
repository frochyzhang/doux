package com.allinfinance.rpc.consumer.provider;

import cn.hutool.core.net.NetUtil;
import com.allinfinance.dev.rpc.scaffold.api.AbstractProcessService;
import com.allinfinance.dev.rpc.scaffold.api.dto.HttpResponseDTO;
import com.allinfinance.dev.rpc.scaffold.api.dto.ProcessRequestDTO;
import com.allinfinance.dev.rpc.scaffold.api.dto.ProcessResponseDTO;
import com.allinfinance.dev.rpc.scaffold.api.dto.RequestTypeEnum;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/1/28 09:51
 */
@Service
public class ProcessServiceImpl extends AbstractProcessService {
    /**
     * 应用系统业务处理服务
     *
     * @param processRequestDTO
     * @return
     */
    @Override
    public ProcessResponseDTO process(ProcessRequestDTO processRequestDTO) {
        System.out.println("请求他进来了");
        ProcessResponseDTO processResponseDTO = new ProcessResponseDTO(RequestTypeEnum.HTTP);
        HttpResponseDTO httpResponseDTO = new HttpResponseDTO(NetUtil.getUsableLocalPort(12000, 12999) + "");
        httpResponseDTO.setHeader("name", "zhangyong");
        httpResponseDTO.setHeader("age", "23");
        processResponseDTO.setResponseDTO(httpResponseDTO);
        return processResponseDTO;
    }
}
