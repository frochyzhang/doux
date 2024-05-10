package cn.lezoo.doux.infrastructure.socket.server.mina.coder;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * @author 张勇
 * @date 2020-11-28 01:24
 */
public class MessageCodecFactory extends DemuxingProtocolCodecFactory {

    public MessageCodecFactory(MessageDecoder decoder,
                               MessageEncoder<String> encoder) {
        addMessageDecoder(decoder);
        addMessageEncoder(String.class, encoder);
    }
}
