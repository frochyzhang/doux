package com.allinfinance.dev.hsp.service;

/**
 * 密钥类型
 * 
 * @author zhangyong
 *
 */
public enum KeyType {
	
	MAK((byte) 0x12), PIK((byte) 0x11);
	
	private byte value;

	private KeyType(byte value) {
		this.value = value;
	}

	/**
	 * 
	 * @return 加密机报文中使用的类型值
	 */
	public byte getValue() {
		return value;
	}

}
