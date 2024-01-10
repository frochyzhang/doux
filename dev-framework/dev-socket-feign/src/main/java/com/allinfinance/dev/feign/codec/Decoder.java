package com.allinfinance.dev.feign.codec;

import com.allinfinance.dev.common.util.xml.jackson.JacksonUtils;

import java.lang.reflect.Type;

public interface Decoder {
    <T> T decode(String data, Type returnType);

    class Default implements Decoder {
        @Override
        public <T> T decode(String data, Type returnType) {
            return JacksonUtils.fromXml(data, returnType);
        }
    }
}
