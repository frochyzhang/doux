package com.allinfinance.dev.hsp.command;

import com.allinfinance.dev.hsp.service.KeyType;
import com.allinfinance.dev.hsp.service.RandomKey;

import java.rmi.ConnectException;

/**
 * 加密机指令接口
 * 
 * @author zhangyong
 * 
 */
public interface HsmCommand {

	/**
	 * 密钥转换，将ZMK加密的密钥转换为LMK加密的密钥
	 * 
	 * @param keyType
	 *            密钥类型
	 * @param zmkIndex
	 *            ZMK索引
	 * @param key
	 *            待转换的密钥
	 * 
	 * @return 转换后的密钥
	 * 
	 * @throws Exception
	 *             加密机返回的异常
	 * @throws ConnectException
	 *             加密机连接异常
	 */
	public byte[] transformKey(KeyType keyType, byte[] zmkIndex, byte[] key) throws Exception;

	/**
	 * 将明文密钥转换为LMK加密的密钥
	 * 
	 * @param keyType
	 *            密钥的类型
	 * @param key
	 *            明文密钥
	 * @return LMK加密的密钥
	 */
	public byte[] transformLmkKey(KeyType keyType, byte[] key) throws Exception;

	/**
	 * 计算MAC
	 * 
	 * @param key
	 *            由LMK加密的工作密钥
	 * @param mab
	 *            计算MAC所用的数据块
	 * 
	 * @return 计算得到的MAC
	 * 
	 * @throws Exception
	 *             加密机返回的异常
	 * @throws ConnectException
	 *             加密机连接异常
	 */
	public byte[] genrateMAC(byte[] key, byte[] mab) throws Exception;

	/**
	 * 验证MAC
	 * 
	 * @param key
	 *            由LMK加密的工作密钥
	 * @param mab
	 *            计算MAC所用的数据块
	 * @param mac
	 *            需要验证的MAC
	 * 
	 * @return 校验结果，成功返回true，失败返回false
	 * @throws Exception
	 *             加密机返回的异常
	 * @throws ConnectException
	 *             加密机连接异常
	 */
	public boolean validateMAC(byte[] key, byte[] mab, byte[] mac) throws Exception;

	/**
	 * PIN转换
	 * 
	 * @param srcKey
	 *            源工作密钥
	 * @param destKey
	 *            目标工作密钥
	 * @param srcFormat
	 *            源格式 (0x01:带主帐号的ANSI9.8格式 0x02: Docutel ATM； 0x03: Diebold and
	 *            IBM ATM,； 0x04: Plus Network； 0x05: ISO9564-1-1格式，1NP..PR...R
	 *            0x06:不带主帐号的ANSI9.8格式 )
	 * @param destFormat
	 *            目标格式 (0x01:带主帐号的ANSI9.8格式 0x02: Docutel ATM； 0x03: Diebold and
	 *            IBM ATM,； 0x04: Plus Network； 0x05: ISO9564-1-1格式，1NP..PR...R
	 *            0x06:不带主帐号的ANSI9.8格式 )
	 * @param srcPIN
	 *            PIN数据块
	 * @param srcAccount
	 *            源账号
	 * @param destAccount
	 *            目标账号
	 * 
	 * @return 转换后的PIN密文
	 * 
	 * @throws Exception
	 *             加密机返回的异常
	 * @throws ConnectException
	 *             加密机连接异常
	 */
	byte[] transformPIN(byte[] srcKey, byte[] destKey, byte srcFormat, byte destFormat, byte[] srcPIN, String srcAccount, String destAccount) throws Exception;

	/**
	 * 计算CVV、CVV2（区别在于服务代码不同）
	 * 
	 * @param cardNo
	 *            介质卡号16位
	 * @param exprDate
	 *            介质有效期，4位，年份月份
	 * @param servCode
	 *            服务商代码3位
	 * 
	 * @return 通过运算得到的CVV值
	 * 
	 * @throws Exception
	 *             加密机返回的异常
	 * @throws ConnectException
	 *             加密机连接异常
	 */
	public byte[] genrateCVV(byte[] CVKA, byte[] CVKB, byte[] cardNo, byte[] splitSym, byte[] exprDate, byte[] servCode) throws Exception;

	/**
	 * 验证CVV、CVV2（服务代码不同）
	 * 
	 * @param cardNo
	 *            介质卡号16位
	 * @param exprDate
	 *            介质有效期，4位，年份月份
	 * @param servCode
	 *            服务商代码3位
	 * @param CVV
	 *            从TPS获取的CVV值
	 * 
	 * @return 验证通过返回true，否则返回false
	 * 
	 * @throws Exception
	 *             加密机返回的异常
	 * @throws ConnectException
	 *             加密机连接异常
	 */
	public boolean validateCVV(byte[] CVKA, byte[] CVKB, byte[] CVV, byte[] cardNo, byte[] splitSym, byte[] exprDate, byte[] servCode) throws Exception;

	/**
	 * 解密PIN
	 * 
	 * @param pinBlock
	 *            从TPS获取
	 * @param cardNo
	 *            从报文获取
	 * 
	 * @return 解密后的pin
	 * 
	 * @throws Exception
	 *             加密机返回的异常
	 * @throws ConnectException
	 *             加密机连接异常
	 */
	public byte[] decryPIN(byte[] pik, byte[] pinBlock, byte[] pinFormat, byte[] cardNo) throws Exception;

	/**
	 * 计算PINOFFSET
	 * 
	 * @param cardNo
	 *            介质卡号16位，使用时需要进行截取
	 * @param pinBlock
	 *            pin值
	 * 
	 * @return 通过运算得到的PINOFFSET值
	 * 
	 * @throws Exception
	 *             加密机返回的异常
	 * @throws ConnectException
	 *             加密机连接异常
	 */
	public byte[] genratePINOFFSET(byte[] pik, byte[] pvk, byte[] pinBlock, byte[] pinFormat, byte[] cardNo, byte[] asciiCode, byte[] pinEffect) throws Exception;

	/**
	 * 验证PINOFFSET
	 * 
	 * @param pinBlock
	 *            从TPS获取
	 * @param pinOffSet
	 *            从数据库获取
	 * @param cardNo
	 *            从报文获取
	 * 
	 * @return 验证PINOFFSET是否正确
	 * 
	 * @throws Exception
	 *             加密机返回的异常
	 * @throws ConnectException
	 *             加密机连接异常
	 */
	public boolean validatePINOFFSET(byte[] pik, byte[] pvk, byte[] pinBlock, byte[] pinFormat, byte[] cardNo, byte[] asciiCode, byte[] pinEffect, byte[] pinOffSet) throws Exception,
			ConnectException;
	
	public RandomKey generateRandomKey(KeyType keyType, byte[] zmkIndex, int keyLength) throws Exception;
	
	/**
	 * 密钥转换，将ZMK加密的密钥转换为LMK加密的密钥(SM4算法)
	 * 
	 * @param zmkIndex
	 *            ZMK索引
	 * @param key
	 *            待转换的密钥
	 * 
	 * @return 转换后的密钥
	 * 
	 * @throws Exception
	 *             加密机返回的异常
	 * @throws ConnectException
	 *             加密机连接异常
	 */
	public byte[] transformKeyBySM4(byte[] zmkIndex, byte[] key) throws Exception;
	
	/**
	 * 计算MAC(SM4算法)
	 * 
	 * @param key
	 *            由LMK加密的工作密钥
	 * @param mab
	 *            计算MAC所用的数据块
	 * 
	 * @return 计算得到的MAC
	 * 
	 * @throws Exception
	 *             加密机返回的异常
	 * @throws ConnectException
	 *             加密机连接异常
	 */
	public byte[] genrateMACBySM4(byte[] key, byte[] mab) throws Exception;
	
	/**
	 * 验证MAC(SM4算法)
	 * 
	 * @param key
	 *            由LMK加密的工作密钥
	 * @param mab
	 *            计算MAC所用的数据块
	 * @param mac
	 *            需要验证的MAC
	 * 
	 * @return 校验结果，成功返回true，失败返回false
	 * @throws Exception
	 *             加密机返回的异常
	 * @throws ConnectException
	 *             加密机连接异常
	 */
	public boolean validateMACBySM4(byte[] key, byte[] mab, byte[] mac) throws Exception;
	
	/**
	 * PIN转换(SM4转3DES)
	 * 
	 * @param srcKey
	 *            源工作密钥
	 * @param destKey
	 *            目标工作密钥
	 * @param srcFormat(无用)
	 *            源格式 (0x01:带主帐号的ANSI9.8格式 0x02: Docutel ATM； 0x03: Diebold and
	 *            IBM ATM,； 0x04: Plus Network； 0x05: ISO9564-1-1格式，1NP..PR...R
	 *            0x06:不带主帐号的ANSI9.8格式 )
	 * @param destFormat(无用)
	 *            目标格式 (0x01:带主帐号的ANSI9.8格式 0x02: Docutel ATM； 0x03: Diebold and
	 *            IBM ATM,； 0x04: Plus Network； 0x05: ISO9564-1-1格式，1NP..PR...R
	 *            0x06:不带主帐号的ANSI9.8格式 )
	 * @param srcPIN
	 *            PIN数据块
	 * @param srcAccount
	 *            源账号
	 * @param destAccount
	 *            目标账号
	 * 
	 * @return 转换后的PIN密文
	 * 
	 * @throws Exception
	 *             加密机返回的异常
	 * @throws ConnectException
	 *             加密机连接异常
	 */
	byte[] transformPINBySM4to3DES(byte[] srcKey, byte[] destKey, byte srcFormat, byte destFormat, byte[] srcPIN, String srcAccount, String destAccount) throws Exception;
	
	/**
	 * PIN转换(3DES转SM4)
	 * 
	 * @param srcKey
	 *            源工作密钥
	 * @param destKey
	 *            目标工作密钥
	 * @param srcFormat(无用)
	 *            源格式 (0x01:带主帐号的ANSI9.8格式 0x02: Docutel ATM； 0x03: Diebold and
	 *            IBM ATM,； 0x04: Plus Network； 0x05: ISO9564-1-1格式，1NP..PR...R
	 *            0x06:不带主帐号的ANSI9.8格式 )
	 * @param destFormat(无用)
	 *            目标格式 (0x01:带主帐号的ANSI9.8格式 0x02: Docutel ATM； 0x03: Diebold and
	 *            IBM ATM,； 0x04: Plus Network； 0x05: ISO9564-1-1格式，1NP..PR...R
	 *            0x06:不带主帐号的ANSI9.8格式 )
	 * @param srcPIN
	 *            PIN数据块
	 * @param srcAccount
	 *            源账号
	 * @param destAccount
	 *            目标账号
	 * 
	 * @return 转换后的PIN密文
	 * 
	 * @throws Exception
	 *             加密机返回的异常
	 * @throws ConnectException
	 *             加密机连接异常
	 */
	byte[] transformPINBy3DEStoSM4(byte[] srcKey, byte[] destKey, byte srcFormat, byte destFormat, byte[] srcPIN, String srcAccount, String destAccount) throws Exception;
	
	public RandomKey generateRandomKeyBySM4(byte[] zmkIndex, int keyLength) throws Exception;

}
