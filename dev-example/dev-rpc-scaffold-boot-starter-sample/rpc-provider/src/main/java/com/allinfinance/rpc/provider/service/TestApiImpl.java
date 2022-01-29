package com.allinfinance.rpc.provider.service;

import com.allinfinance.rpc.scaffold.api.TestApi;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2021/12/30 22:17
 */
@Service
public class TestApiImpl implements TestApi {
    @Override
    public String message() {
        System.out.println("alsdjfalskdf");
        return "hello test";
    }
}
