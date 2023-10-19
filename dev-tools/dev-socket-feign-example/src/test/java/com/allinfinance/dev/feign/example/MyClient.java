package com.allinfinance.dev.feign.example;

import com.allinfinance.dev.feign.Client;
import com.allinfinance.dev.feign.Request;
import com.allinfinance.dev.framework.extension.annotation.Extension;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2023/10/18 21:45
 */
@Extension("default")
public class MyClient implements Client {
    @Override
    public Object execute(Request request) throws Exception {
        System.out.println("我是自定义client default");
        return request.getData();
    }
}
