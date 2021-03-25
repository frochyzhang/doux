package com.allinfinance.dev.hsp.service;

import java.io.Serializable;

/**
 * 
 * @author zhangyong
 *	存储返回的新秘钥
 */
public class RandomKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7161506562681771688L;

	public byte[] wkUnderZMK;
	
	public byte[] wkCheckValue;

	public RandomKey(byte[] wkUnderZMK, byte[] wkCheckValue) {
		super();
		this.wkUnderZMK = wkUnderZMK;
		this.wkCheckValue = wkCheckValue;
	}

}
