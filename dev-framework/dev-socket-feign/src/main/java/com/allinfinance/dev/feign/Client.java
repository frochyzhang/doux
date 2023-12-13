package com.allinfinance.dev.feign;

import com.allinfinance.dev.common.socket.api.client.dto.SocketRequestDTO;
import com.allinfinance.dev.common.util.convert.PropertiesParseUtils;
import com.allinfinance.dev.feign.codec.Decoder;
import com.allinfinance.dev.feign.codec.Encoder;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;
import com.allinfinance.dev.framework.socket.client.driver.SocketClient;

import java.util.Properties;

public interface Client {
    void setEncoder(Encoder encoder);

    void setDecoder(Decoder decoder);

    <T> T execute(SocketRequestDTO connectParams, Object data, Class<T> returnType) throws Exception;

    class Default implements Client {

        private Encoder encoder = new Encoder.Default();

        private Decoder decoder = new Decoder.Default();

        public Encoder getEncoder() {
            return encoder;
        }

        public Decoder getDecoder() {
            return decoder;
        }

        @Override
        public void setEncoder(Encoder encoder) {
            this.encoder = encoder;
        }

        @Override
        public void setDecoder(Decoder decoder) {
            this.decoder = decoder;
        }

        @Override
        public <T> T execute(SocketRequestDTO connectParams, Object data, Class<T> returnType) throws Exception {
            Properties properties = new Properties();
            PropertiesParseUtils.fromBean(properties, connectParams);
            String xml = encoder.encode(data);
            String result = ExtensionLoaderFactory.getExtension(SocketClient.class, "default")
                    .send(properties, xml);
            return decoder.decode(result, returnType);
        }
    }
}
