package com.allinfinance.dev.socket.codec;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * @author 张勇
 * @date 2020-11-28 01:24
*/
public class MessageCodecFactory extends DemuxingProtocolCodecFactory {

	public MessageCodecFactory(MessageDecoder decoder,
			MessageEncoder encoder) {
		addMessageDecoder(decoder);
		addMessageEncoder(Object.class, encoder);
	}
}
