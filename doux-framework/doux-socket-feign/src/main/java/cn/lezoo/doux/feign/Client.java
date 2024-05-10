package cn.lezoo.doux.feign;

import cn.lezoo.doux.common.socket.api.client.dto.SocketRequestDTO;
import cn.lezoo.doux.common.util.convert.PropertiesParseUtils;
import cn.lezoo.doux.feign.codec.Decoder;
import cn.lezoo.doux.feign.codec.Encoder;
import cn.lezoo.doux.framework.extension.loader.ExtensionLoaderFactory;
import cn.lezoo.doux.framework.socket.client.driver.SocketClient;

import java.lang.reflect.Type;
import java.util.Properties;

public interface Client {
    void setEncoder(Encoder encoder);

    void setDecoder(Decoder decoder);

    <T> T execute(SocketRequestDTO connectParams, Object data, Type returnType) throws Exception;

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
        public <T> T execute(SocketRequestDTO connectParams, Object data, Type returnType) throws Exception {
            Properties properties = new Properties();
            PropertiesParseUtils.fromBean(properties, connectParams);
            String xml = encoder.encode(data);
            String result = ExtensionLoaderFactory.getExtension(SocketClient.class, "default")
                    .send(properties, xml);
            return decoder.decode(result, returnType);
        }
    }
}
