package com.allinfinance.dev.feign;

import com.allinfinance.dev.framework.extension.annotation.Extensible;

@Extensible(singleton = false)
public interface Client {
    Object execute(Request request) throws Exception;
}
