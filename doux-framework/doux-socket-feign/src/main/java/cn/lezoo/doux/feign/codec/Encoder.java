package cn.lezoo.doux.feign.codec;

import cn.lezoo.doux.common.util.xml.jackson.JacksonUtils;

public interface Encoder {
    String encode(Object data);

    class Default implements Encoder {
        @Override
        public String encode(Object data) {
            return JacksonUtils.toXml(data);
        }
    }
}
