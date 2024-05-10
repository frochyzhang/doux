package cn.lezoo.doux.feign;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

public interface InvocationHandlerFactory {
    InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch);

    interface MethodHandler {

        <T> T invoke(Object[] argv, Type returnType) throws Throwable;
    }


    final class Default implements InvocationHandlerFactory {

        @Override
        public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
            return new ReflectiveFeign.FeignInvocationHandler(target, dispatch);
        }
    }
}
