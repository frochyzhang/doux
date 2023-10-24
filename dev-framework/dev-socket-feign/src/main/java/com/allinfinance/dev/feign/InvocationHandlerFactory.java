package com.allinfinance.dev.feign;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public interface InvocationHandlerFactory {
    InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch);

    interface MethodHandler {

        <T> T invoke(Object[] argv, Class<T> returnType) throws Throwable;
    }


    final class Default implements InvocationHandlerFactory {

        @Override
        public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
            return new ReflectiveFeign.FeignInvocationHandler(target, dispatch);
        }
    }
}
