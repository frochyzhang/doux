package com.allinfinance.dev.core.hsp;

/**
 * 密钥类型
 *
 * @author zhangyong
 */
public enum KeyType {
    /**
     * MAK keyType
     */
    MAK((byte) 0x12),
    /**
     * PIK keyType
     */
    PIK((byte) 0x11);

    private final byte value;

    KeyType(byte value) {
        this.value = value;
    }

    /**
     * @return 加密机报文中使用的类型值
     */
    public byte getValue() {
        return value;
    }

}
