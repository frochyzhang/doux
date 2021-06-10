package com.allinfinance.dev.hsp.utils;

import com.allinfinance.dev.hsp.exception.CodecException;
import org.apache.commons.codec.DecoderException;

/**
 * Classname  com.allinfinance.dev.hsp.utils.LengthType
 *
 * @Description TODO
 * @Date 2021/3/25 9:33
 * @Created by ZhangYong
 */

public enum LengthType {
    INT {
        @Override
        public int getValueLengthByType(byte[] lengthBytes) throws IllegalArgumentException, CodecException {
            return CodecUtil.bytesToInt(lengthBytes);
        }

        @Override
        public byte[] getBytesByLength(int lengthSize, int value) throws CodecException {
            if (lengthSize != 1 && lengthSize != 2 && lengthSize != 4) {
                throw new CodecException("INT类型的长度容量必须为1,2,4中的一个");
            } else {
                byte[] result = new byte[]{(byte)(value >>> 24 & 255), (byte)(value >>> 16 & 255), (byte)(value >>> 8 & 255), (byte)(value >>> 0 & 255)};
                byte[] tempBytes = new byte[lengthSize];

                for(int i = tempBytes.length - 1; i >= 0; --i) {
                    tempBytes[i] = result[4 - (tempBytes.length - i)];
                }

                return tempBytes;
            }
        }
    },
    STRING {
        @Override
        public int getValueLengthByType(byte[] lengthBytes) throws IllegalArgumentException {
            return Integer.parseInt(new String(lengthBytes));
        }

        @Override
        public byte[] getBytesByLength(int lengthSize, int value) throws CodecException {
            return String.format("%0" + lengthSize + "d", value).getBytes();
        }
    },
    BCD {
        @Override
        public int getValueLengthByType(byte[] lengthBytes) throws IllegalArgumentException {
            throw new IllegalArgumentException("不支持的长度类型[" + this.toString() + "]");
        }

        @Override
        public byte[] getBytesByLength(int lengthSize, int value) throws CodecException {
            try {
                return CodecUtil.intToBCD(value, lengthSize);
            } catch (DecoderException var4) {
                throw new CodecException("BCD类型的长度容量必须为偶数", var4);
            }
        }
    },
    FOURBCD {
        @Override
        public int getValueLengthByType(byte[] lengthBytes) throws IllegalArgumentException {
            throw new IllegalArgumentException("不支持的长度类型[" + this.toString() + "]");
        }

        @Override
        public byte[] getBytesByLength(int lengthSize, int value) throws CodecException {
            return STRING.getBytesByLength(lengthSize, value);
        }
    };

    LengthType() {
    }

    public abstract int getValueLengthByType(byte[] var1) throws IllegalArgumentException, CodecException;

    public abstract byte[] getBytesByLength(int var1, int var2) throws CodecException;
}

