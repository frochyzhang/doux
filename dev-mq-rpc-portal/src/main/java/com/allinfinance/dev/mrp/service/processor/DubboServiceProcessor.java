package com.allinfinance.dev.mrp.service.processor;

import com.allinfinance.dev.core.loader.SpringConfigTool;
import com.allinfinance.dev.mrp.param.GenericReponse;
import com.allinfinance.dev.mrp.param.RequestParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2021/8/9 14:07
 */
@Slf4j
@Component
public class DubboServiceProcessor extends BaseBusinessProcessor {
    @Override
    public String supportType() {
        return RequestParams.RPC_SWITCH;
    }

    @Override
    public <T> GenericReponse<T> process(Object req, RequestParams requestParams) {
        GenericReponse<T> response = new GenericReponse<>(Boolean.TRUE);
        /*
         * 1、根据接口类获取refference bean
         * 2、根据rpc方法，请求类型，请求体反射调用
         */
        try {
            Object beanByClassName = SpringConfigTool.getBeanByClassName(requestParams.getRpcInterface());
            T invokeResp = (T) Class.forName(requestParams.getRpcInterface())
                    .getDeclaredMethod(requestParams.getRpcMethod(), req.getClass())
                    .invoke(beanByClassName, req);
            response.setRpcInvokeData(invokeResp);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("rpc 调用失败!", e);
            response.setRespStatus(Boolean.FALSE);
        }
        return response;
    }
}
