package com.allinfinance.dev.feign;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2023/10/18 14:58
 */
public class ReflectiveFeign {
    InvocationHandlerFactory factory = new InvocationHandlerFactory.Default();
    SynchronousMethodHandler.Factory methodHandlerFactory;

    public ReflectiveFeign(Client client) {
        this.methodHandlerFactory = new SynchronousMethodHandler.Factory(client);
    }

    public <T> T newInstance(Target<T> target) {
        Map<Method, InvocationHandlerFactory.MethodHandler> dispatch = new LinkedHashMap<>();
        Method[] methods = target.type().getMethods();
        if (Arrays.stream(methods).noneMatch(method -> method.isAnnotationPresent(DevRequestLine.class))) {
            throw new IllegalArgumentException("@" + target.type().getName() + "@没有找到RequestLine注解");
        }
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(DevRequestLine.class))
                .forEach(method -> {
                    dispatch.put(method, methodHandlerFactory.create(target));
                });
        InvocationHandler handler = factory.create(target, dispatch);
        return (T) Proxy.newProxyInstance(target.type().getClassLoader(), new Class[]{target.type()}, handler);
    }

    public static class FeignInvocationHandler implements InvocationHandler {
        private final Target target;
        private final Map<Method, InvocationHandlerFactory.MethodHandler> dispatch;

        public FeignInvocationHandler(Target target, Map<Method, InvocationHandlerFactory.MethodHandler> dispatch) {
            this.target = target;
            this.dispatch = dispatch;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            switch (method.getName()) {
                case "equals":
                    try {
                        Object otherHandler =
                                args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
                        return equals(otherHandler);
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                case "hashCode":
                    return hashCode();
                case "toString":
                    return toString();
                default:
            }

            return dispatch.get(method).invoke(args, method.getReturnType());
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof FeignInvocationHandler) {
                FeignInvocationHandler other = (FeignInvocationHandler) obj;
                return target.equals(other.target);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return target.hashCode();
        }

        @Override
        public String toString() {
            return target.toString();
        }
    }
}
