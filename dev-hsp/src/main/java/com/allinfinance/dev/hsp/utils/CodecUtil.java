package com.allinfinance.dev.hsp.utils;

import com.allinfinance.dev.hsp.exception.CodecException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;

/**
 * Classname  com.allinfinance.dev.hsp.CodecUtil
 *
 * @Description TODO
 * @Date 2021/3/25 9:30
 * @Created by ZhangYong
 */
public class CodecUtil {
    public static ByteBuffer lvBytes(byte[] bytes, LengthType lengthType, int lengthSize) throws CodecException {
        if (lengthSize <= 0) {
            return ByteBuffer.wrap(bytes);
        } else {
            ByteBuffer buff;
            byte[] length;
            buff = ByteBuffer.allocate(lengthSize + bytes.length);
            label34:
            switch(lengthType) {
                case STRING:
                    length = String.format("%0" + lengthSize + "d", bytes.length).getBytes();
                    break;
                case INT:
                    if (lengthSize != 1 && lengthSize != 2 && lengthSize != 4) {
                        throw new CodecException("INT类型的长度容量必须为1,2,4中的一个");
                    }

                    length = new byte[lengthSize];
                    int i = 0;

                    while(true) {
                        if (i >= lengthSize) {
                            break label34;
                        }

                        length[i] = (byte)(bytes.length >> (lengthSize - i - 1) * 8);
                        ++i;
                    }
                case BCD:
                    try {
                        length = intToBCD(bytes.length, lengthSize);
                        break;
                    } catch (DecoderException var6) {
                        throw new CodecException("BCD类型的长度容量必须为偶数", var6);
                    }
                default:
                    throw new IllegalArgumentException("不支持的长度类型[" + lengthType + "]");
            }

            buff.put(length);
            buff.put(bytes);
            buff.flip();
            return buff;
        }
    }

    public static byte[] intToBCD(int num, int length) throws DecoderException {
        String strLen = String.format("%0" + length + "d", num);
        byte[] result = Hex.decodeHex(strLen.toCharArray());
        return result;
    }

    public static int bytesToInt(byte[] bytes) throws CodecException {
        if (bytes.length != 1 && bytes.length != 2 && bytes.length != 4) {
            throw new CodecException("INT类型的byte数组长度必须为1,2,4中的一个");
        } else {
            int n = 0;

            for(int i = 0; i < bytes.length; ++i) {
                n <<= 8;
                n |= bytes[i] & 255;
            }

            return n;
        }
    }
}
