package com.allinfinance.dev.core.util.convert.simple8583.type;

/**
 * <p>
 * .
 * </p>
 *
 */
public class LLVAR implements IsoType {

	@Override
	public boolean isLVar() {
		return false;
	}

	@Override
	public String setByteValue(byte[] bts) {
		return null;
	}

	@Override
	public byte[] setValue(String bts) {
		return new byte[0];
	}

	@Override
	public int varLength(byte[] bytes) {
		return 0;
	}

}
