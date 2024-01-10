package com.allinfinance.dev.feign.codec;

import com.allinfinance.dev.common.util.xml.jackson.JacksonUtils;

public interface Encoder {
    String encode(Object data);

    class Default implements Encoder {
        @Override
        public String encode(Object data) {
            return JacksonUtils.toXml(data);
        }
    }
}
