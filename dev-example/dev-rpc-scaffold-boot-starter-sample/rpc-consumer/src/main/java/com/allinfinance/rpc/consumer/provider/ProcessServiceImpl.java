package com.allinfinance.rpc.consumer.provider;

import com.allinfinance.dev.rpc.scaffold.api.ProcessService;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/1/28 09:51
 */
@Service
public class ProcessServiceImpl implements ProcessService {

    /**
     * 注册到网关后的验证接口
     *
     * @return
     */
    @Override
    public Boolean verify() {
        return Boolean.TRUE;
    }

    /**
     * 应用系统业务处理服务
     *
     * @param process
     * @return
     */
    @Override
    public String process(String process) {
        return null;
    }
}
