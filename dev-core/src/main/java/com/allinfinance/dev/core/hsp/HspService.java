package com.allinfinance.dev.core.hsp;


/**
 * 加密平台服务，提供有关加密机运算的API
 *
 * @author zhangyong
 */
public interface HspService {

    /**
     * 验证MAC，用于普通交易
     *
     * @param keyIndex  MAK密钥索引
     * @param mac       MAC值
     * @param mab       用于MAC运算的MAB
     * @param algorithm 用于区分加密算法
     * @return 验证通过返回true，否则返回false
     */
    boolean validateMACByAlgorithm(String keyIndex, byte[] mac, byte[] mab, EncryptAlgorithm algorithm);

    /**
     * 验证MAC，用于密钥重置交易
     *
     * @param keyIndex  MAK密钥索引
     * @param macKey    MAC密钥
     * @param mac       MAC值
     * @param mab       用于MAC运算的MAB
     * @param algorithm 用于区分加密算法
     * @return 验证通过返回true，否则返回false
     */
    boolean validateMACByAlgorithm(String keyIndex, byte[] macKey, byte[] mac, byte[] mab, EncryptAlgorithm algorithm);

    /**
     * 生成MAC，用于普通交易
     *
     * @param keyIndex  MAK密钥索引
     * @param mab       用于MAC运算的MAB
     * @param algorithm 用于区分加密算法
     * @return 通过MAB运算得到的MAC值
     */
    public byte[] genrateMACByAlgorithm(String keyIndex, byte[] mab, EncryptAlgorithm algorithm);

    /**
     * 生成MAC，用于密钥重置交易
     *
     * @param keyIndex  MAK密钥索引
     * @param macKey    MAC密钥
     * @param mab       用于MAC运算的MAB
     * @param algorithm 用于区分加密算法
     * @return 通过MAB运算得到的MAC值
     */
    byte[] genrateMACByAlgorithm(String keyIndex, byte[] macKey, byte[] mab, EncryptAlgorithm algorithm);

    /**
     * PIN转换
     *
     * @param srcKeyIndex  源PIK密钥索引
     * @param destKeyIndex 目标PIK密钥索引
     * @param srcFormat    源格式 (0x01:带主帐号的ANSI9.8格式 0x02: Docutel ATM； 0x03: Diebold and
     *                     IBM ATM,； 0x04: Plus Network； 0x05: ISO9564-1-1格式，1NP..PR...R
     *                     0x06:不带主帐号的ANSI9.8格式 )
     * @param destFormat   目标格式 (0x01:带主帐号的ANSI9.8格式 0x02: Docutel ATM； 0x03: Diebold and
     *                     IBM ATM,； 0x04: Plus Network； 0x05: ISO9564-1-1格式，1NP..PR...R
     *                     0x06:不带主帐号的ANSI9.8格式 )
     * @param srcPIN       源PIN
     * @param srcAccount   源账号
     * @param destAccount  目标账号
     * @param from         转换前加密算法
     * @param to           转换后加密算法
     * @return 转换后得到的目标PIN
     */
    byte[] transformPINByAlgorithm(String srcKeyIndex, String destKeyIndex, byte srcFormat, byte destFormat, byte[] srcPIN, String srcAccount, String destAccount,
                                   EncryptAlgorithm from, EncryptAlgorithm to);

    /**
     * 注册密钥
     *
     * @param keyIndex  要注册的密钥索引
     * @param keyType   密钥的类型
     * @param key       密钥
     * @param algorithm 用于区分加密算法
     */
    void registerKeyByAlgorithm(String keyIndex, KeyType keyType, byte[] key, EncryptAlgorithm algorithm);

    /**
     * 生成CVV
     *
     * @param cardNo   介质卡号16位
     * @param exprDate 介质有效期，4位，年份月份
     * @param servCode 服务商代码3位
     * @return 通过运算得到的CVV值
     */
    String genrateCVV(String cardNo, String exprDate, String servCode, EncryptIndex encry);

    /**
     * 验证CVV
     *
     * @param cardNo   介质卡号16位
     * @param exprDate 介质有效期，4位，年份月份
     * @param servCode 服务商代码3位
     * @param cvv      从TPS获取的CVV值
     * @return 验证通过返回true，否则返回false
     */
    boolean validateCVV(String cardNo, String exprDate, String servCode, String cvv, EncryptIndex encry);

    /**
     * 生成CVV2
     *
     * @param cardNo   介质卡号16位
     * @param exprDate 介质有效期，4位，年份月份
     * @return 通过运算得到的CVV2值
     */
    String genrateCVV2(String cardNo, String exprDate, EncryptIndex encry);

    /**
     * 验证CVV2
     *
     * @param cardNo   介质卡号16位
     * @param exprDate 介质有效期，4位，年份月份
     * @param encry    服务商代码3位 ,000
     * @param cvv2     从TPS获取的CVV2值
     * @return 验证通过返回true，否则返回false
     */
    boolean validateCVV2(String cardNo, String exprDate, String cvv2, EncryptIndex encry);

    /**
     * 生成PIN OFFSET
     *
     * @param cardNo   介质卡号16位
     * @param pinBlock pin密文，从TPS获取后再进行转换（0x05A6）
     * @return 通过运算得到的PINOFFSET值
     */
    String genratePINOFFSET(String pinBlock, String cardNo, EncryptIndex encry);

    /**
     * 校验PIN OFFSET
     *
     * @param cardNo    介质卡号16位
     * @param pinBlock  pik2 加密后的密文，从TPS获取
     * @param pinOffSet pinOffSet，报文中获取
     * @return PINOFFSET校验是否通过
     */

    boolean validatePINOFFSET(String pinBlock, String cardNo, String pinOffSet, EncryptIndex encry);

    /**
     * 随机产生新的秘钥值，供终端/商户重置秘钥
     *
     * @param keyIndex  MAK密钥索引
     * @param keyType   秘钥类型 1：PIK， 2：MAK
     * @param keyLength 秘钥长度
     * @param algorithm 加密算法
     * @return
     */
    RandomKey generateRandomKeyByAlgorithm(String keyIndex, KeyType keyType, int keyLength, EncryptAlgorithm algorithm);

    /**
     * 根据请求上送的密钥索引及校验值，验证密钥重置交易是否有效。
     *
     * @param keyIndex
     * @param kcvInRequest
     * @return
     */
    Boolean validateZmkKcvByIndex(String keyIndex, String kcvInRequest);
}
