package cn.lezoo.doux.feign.codec;

import cn.lezoo.doux.common.util.xml.jackson.JacksonUtils;

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
