package com.allinfinance.dev.rpc.scaffold.processor;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/16 16:55
 * @deprecated 该抽象类由于springboot2.6.x版本默认禁止了循环依赖，后续直接实现#{@link BusinessProcessor}即可
 */
public abstract class AbstractBusinessProcessor implements BusinessProcessor {
}
