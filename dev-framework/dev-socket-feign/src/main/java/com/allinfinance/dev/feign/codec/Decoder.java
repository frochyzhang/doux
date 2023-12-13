package com.allinfinance.dev.feign.codec;

import com.allinfinance.dev.common.util.xml.xstream.XStreamUtils;

public interface Decoder {
    <T> T decode(String data, Class<T> returnType);

    class Default implements Decoder {
        @Override
        public <T> T decode(String data, Class<T> returnType) {
            return XStreamUtils.xmlToBean(data, returnType);
        }
    }
}
