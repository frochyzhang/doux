package com.allinfinance.dev.rpc.scaffold.api;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/1/28 09:35
 */
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
    Integer init();

    /**
     * 应用系统业务处理服务
     *
     * @param process
     * @return
     */
    String process(String process);
}
