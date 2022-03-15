package com.allinfinance.rpc.consumer.provider;

import com.allinfinance.dev.rpc.scaffold.api.AbstractProcessService;
import com.allinfinance.dev.rpc.scaffold.api.dto.ProcessRequestDTO;
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
    public String process(ProcessRequestDTO processRequestDTO) {
        System.out.println("请求他进来了");
        return "test2";
    }
}
