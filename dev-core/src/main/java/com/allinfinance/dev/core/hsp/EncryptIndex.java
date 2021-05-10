package com.allinfinance.dev.core.hsp;

import java.io.Serializable;

/**
 * 根据mps传输的index，获取对应的pin value
 * @author zhangyong
 */
public class EncryptIndex implements Serializable {

	private static final long serialVersionUID = -3875342676884394942L;
	private String cvkAIndex;//获取cvkA的index
	private String cvkBIndex;//获取cvkB的index
	private String pvkIndex;//获取pvk的index
	private String pikIndex;//获取pik的index
	
	public String getCvkAIndex() {
		return cvkAIndex;
	}
	public void setCvkAIndex(String cvkAIndex) {
		this.cvkAIndex = cvkAIndex;
	}
	public String getCvkBIndex() {
		return cvkBIndex;
	}
	public void setCvkBIndex(String cvkBIndex) {
		this.cvkBIndex = cvkBIndex;
	}
	public String getPvkIndex() {
		return pvkIndex;
	}
	public void setPvkIndex(String pvkIndex) {
		this.pvkIndex = pvkIndex;
	}
	public String getPikIndex() {
		return pikIndex;
	}
	public void setPikIndex(String pikIndex) {
		this.pikIndex = pikIndex;
	}
	
	
}
