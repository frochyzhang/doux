package com.allinfinance.dev.hsp.command.impl;

import com.allinfinance.dev.core.hsp.EncryptAlgorithm;
import com.allinfinance.dev.core.hsp.KeyType;
import com.allinfinance.dev.core.hsp.RandomKey;
import com.allinfinance.dev.hsp.exception.CodecException;
import com.allinfinance.dev.hsp.socket.HsmSocketTest;
import com.allinfinance.dev.hsp.utils.CodecUtil;
import com.allinfinance.dev.hsp.utils.LengthType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;
import java.rmi.ConnectException;

/**
 * Classname  com.allinfinance.dev.hsp.command.impl.HsmCommandImpl
 *
 * @Description TODO
 * @Date 2021/3/24 17:06
 * @Created by ZhangYong
 */
@Slf4j
public class SJL05HsmCommandImpl implements EncryptAlgorithm.HsmCommand {

    /**
     * MAC的长度
     */
    protected static final int MAC_LENGTH = 8;

    /**
     * PIN的长度
     */
    protected static final int PIN_LENGTH = 8;
    /**
     * CVV的长度
     */
    protected static final int CVV_LENGTH = 3;
    /**
     * 转换后的PIN_BLOCK长度
     */
    protected static final int PINBLOCK_LENGTH = 7;
    /**
     * pinoffset长度
     */
    protected static final int PINOFFSET_LENGTH = 12;

    @Override
    public byte[] transformKey(KeyType keyType, byte[] zmkIndex, byte[] key) throws Exception {
        log.debug("组装密钥转换加密机指令");
        // FIXME 暂时写死
        ByteBuffer buff = ByteBuffer.allocate(2048);
        // 转换密钥指令
        byte[] command = {(byte) 0xD1, (byte) 0x02};
        buff.put(command);
        log.debug("指令[D102]");
        // 工作密钥长度
        buff.put((byte) key.length);
        // 通信主密钥长度
        buff.put((byte) 0x10);
        // ZMK INDEX
        buff.put(zmkIndex);
        // 密钥类型
        buff.put(keyType.getValue());
        // 密钥
        buff.put(key);
        log.debug("工作密钥:[{}]", Hex.encodeHexString(key));
        // 写入完毕，翻回
        buff.flip();
        byte[] request = new byte[buff.limit()];
        buff.get(request);
        log.debug("向加密机发送的密钥转换报文为[{}]", Hex.encodeHexString(request));
        byte[] response = new HsmSocketTest().request(request);
        ByteBuffer res = ByteBuffer.wrap(response);
        log.debug("结束组装密钥转换加密机指令");
        if (res.get() != 'A') {
            byte[] errCode = new byte[1];
            res.get(errCode);
            throw new Exception("加密机报错，异常码[" + Hex.encodeHexString(errCode) + "]");
        } else {
            // 得到密钥长度
            byte wkLength = res.get();
            byte[] wk = new byte[wkLength];
            res.get(wk);
            return wk;
        }
    }

    @Override
    public byte[] transformLmkKey(KeyType keyType, byte[] key) throws Exception {
        log.debug("组装密钥转换加密机指令");
        // FIXME 暂时写死
        ByteBuffer buff = ByteBuffer.allocate(2048);
        // 转换密钥指令
        byte[] command = {(byte) 0xD1, (byte) 0x08};
        buff.put(command);
        log.debug("指令[D108]");
        // 工作密钥长度
        buff.put((byte) key.length);
        // 密钥类型
        buff.put(keyType.getValue());
        // 密钥
        buff.put(key);
        log.debug("工作密钥:[{}]", Hex.encodeHexString(key));
        // 写入完毕，翻回
        buff.flip();
        byte[] request = new byte[buff.limit()];
        buff.get(request);
        log.debug("向加密机发送的LMK加密密钥转换报文为[{}]", Hex.encodeHexString(request));
        byte[] response = new HsmSocketTest().request(request);
        ByteBuffer res = ByteBuffer.wrap(response);
        log.debug("结束组装密钥转换加密机指令");
        if (res.get() != 'A') {
            byte[] errCode = new byte[1];
            res.get(errCode);
            throw new Exception("加密机报错，异常码[" + Hex.encodeHexString(errCode) + "]");
        } else {
            // 得到密钥长度
            byte wkLength = res.get();
            byte[] wk = new byte[wkLength];
            res.get(wk);
            log.debug("加密机转换后的LMK加密的工作密钥[{}]", Hex.encodeHexString(wk));
            byte[] checkValue = new byte[8];
            res.get(checkValue);
            log.debug("校验码[{}]", Hex.encodeHexString(checkValue));
            return wk;
        }
    }

    @Override
    public byte[] genrateMAC(byte[] key, byte[] mab) throws Exception {
        log.debug("组装生成MAC加密机指令");
        // FIXME 暂时写死
        ByteBuffer buff = ByteBuffer.allocate(2048);
        // 生成MAC指令
        byte[] command = {(byte) 0xD1, (byte) 0x32};
        buff.put(command);
        log.debug("指令[D132]");
        // MAC算法
        buff.put((byte) 0x02);
        // MAK长度
        buff.put((byte) key.length);
        // MAK
        buff.put(key);
        log.debug("MAK[{}]", Hex.encodeHexString(key));
        // 初始向量
        byte[] initialVector = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        buff.put(initialVector);
        try {
            // 写入LV格式包装后的MAB
            buff.put(CodecUtil.lvBytes(mab, LengthType.INT, 2));
        } catch (CodecException e) {
            log.warn("绝对不应出现的异常", e);
            return null;
        }
        // 写入完毕，翻回
        buff.flip();
        byte[] request = new byte[buff.limit()];
        buff.get(request);
        log.debug("向加密机发送的生成MAC报文为[{}]", Hex.encodeHexString(request));
        byte[] response = new HsmSocketTest().request(request);
        ByteBuffer res = ByteBuffer.wrap(response);
        log.debug("结束组装生成MAC加密机指令");
        if (res.get() != 'A') {
            byte[] errCode = new byte[1];
            res.get(errCode);
            throw new Exception("加密机报错，异常码[" + Hex.encodeHexString(errCode) + "]");
        } else {
            byte[] mac = new byte[MAC_LENGTH];
            res.get(mac);
            log.debug("加密机计算得到的MAC[{}]", Hex.encodeHexString(mac));
            return mac;
        }
    }

    @Override
    public boolean validateMAC(byte[] key, byte[] mab, byte[] mac) throws Exception {
        log.debug("组装验证MAC加密机指令");

        // FIXME 暂时写死
        ByteBuffer buff = ByteBuffer.allocate(2048);
        // 验证MAC指令
        byte[] command = {(byte) 0xD1, (byte) 0x34};
        buff.put(command);
        log.debug("指令[D134]");
        // MAC算法
        buff.put((byte) 0x02);
        // MAK长度
        buff.put((byte) key.length);
        // MAK
        buff.put(key);
        log.debug("MAK[{}]", Hex.encodeHexString(key));
        // 初始向量
        byte[] initialVector = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        buff.put(initialVector);
        // MAC
        buff.put(mac);
        try {
            // 写入LV格式包装后的MAB
            buff.put(CodecUtil.lvBytes(mab, LengthType.INT, 2));
        } catch (CodecException e) {
            log.warn("绝对不应出现的异常", e);
            return false;
        }
        // 写入完毕，翻回
        buff.flip();
        byte[] request = new byte[buff.limit()];
        buff.get(request);

        log.debug("向加密机发送的验证MAC报文为[{}]", Hex.encodeHexString(request));
        byte[] response = new HsmSocketTest().request(request);
        ByteBuffer res = ByteBuffer.wrap(response);
        log.debug("结束组装验证MAC加密机指令");
        if (res.get() != 'A') {
            byte[] errCode = new byte[1];
            res.get(errCode);
            if (errCode[0] != 0x2D) {
                throw new Exception("加密机报错，异常码[" + Hex.encodeHexString(errCode) + "]");
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public byte[] transformPIN(byte[] srcKey, byte[] destKey, byte srcFormat, byte destFormat, byte[] srcPIN, String srcAccount, String destAccount) throws Exception {
        log.debug("组装转PIN加密机指令");

        // FIXME 暂时写死
        ByteBuffer buff = ByteBuffer.allocate(512);
        // 转PIN指令
        byte[] command = {(byte) 0xD1, (byte) 0x24};
        buff.put(command);
        log.debug("指令[D124]");
        // PIK1长度
        buff.put((byte) srcKey.length);
        // PIK1
        buff.put(srcKey);
        log.debug("PIK1[" + Hex.encodeHexString(srcKey) + "]");
        // PIK2长度
        buff.put((byte) destKey.length);
        // PIK2
        buff.put(destKey);
        log.debug("PIK2[" + Hex.encodeHexString(destKey) + "]");
        // 转换前PINBLOCK格式
        buff.put(srcFormat);
        // 转换后PINBLOCK格式
        buff.put(destFormat);
        // PIN密文
        buff.put(srcPIN);
        // 源账号
        buff.put(srcAccount.getBytes());
        buff.put((byte) ';');
        // 目标账号
        buff.put(destAccount.getBytes());
        buff.put((byte) ';');
        // 写入完毕，翻回
        buff.flip();
        byte[] msg = new byte[buff.limit()];
        buff.get(msg);

        log.debug("向加密机发送的PIN转换报文为[{}]", Hex.encodeHexString(msg));
        byte[] response = new HsmSocketTest().request(msg);
        ByteBuffer res = ByteBuffer.wrap(response);
        log.debug("结束组装转PIN加密机指令");
        if (res.get() != 'A') {
            byte[] errCode = new byte[1];
            res.get(errCode);
            throw new Exception("加密机报错，异常码[" + Hex.encodeHexString(errCode) + "]");
        } else {
            byte[] pin = new byte[PIN_LENGTH];
            res.get(pin);
            log.debug("加密机计算得到的PIN[{}]", Hex.encodeHexString(pin));
            return pin;
        }
    }

    @Override
    public byte[] genrateCVV(byte[] CVKA, byte[] CVKB, byte[] cardNo, byte[] splitSym, byte[] exprDate, byte[] servCode) throws Exception {
        log.info("starting generateCVV");
        // 生成CVV。使用新指令

        // FIXME 暂时写死
        ByteBuffer buff = ByteBuffer.allocate(512);
        // 生成CVV指令
        buff.put((byte) 0xD1);
        buff.put((byte) 0x42);
        // 写入CVKA
        buff.put(CVKA);
        // 写入CVKB
        buff.put(CVKB);
        // 写入卡号
        buff.put(cardNo);
        // 写入分隔符
        buff.put(splitSym);
        // 写入有效期
        buff.put(exprDate);
        // 写入服务商代码
        buff.put(servCode);

        // 写入完毕，翻回
        buff.flip();
        byte[] request = new byte[buff.limit()];
        buff.get(request);
        log.debug("向加密机发送的genrateCVV转换报文为[{}]", Hex.encodeHexString(request));

        byte[] response = new HsmSocketTest().request(request);
        ByteBuffer res = ByteBuffer.wrap(response);
        log.info("end generateCVV");
        if (res.get() != 'A') {
            byte[] errCode = new byte[1];
            res.get(errCode);
            throw new Exception("genrateCVV加密机报错，异常码[" + Hex.encodeHexString(errCode) + "]");
        } else {
            byte[] cvv = new byte[CVV_LENGTH];
            res.get(cvv);
            return cvv;
        }
    }

    @Override
    public boolean validateCVV(byte[] CVKA, byte[] CVKB, byte[] CVV, byte[] cardNo, byte[] splitSym, byte[] exprDate, byte[] servCode) throws Exception {
        log.info("starting validate CVV");
        // 验证CVV。使用新指令校验CVV

        // FIXME 暂时写死
        ByteBuffer buff = ByteBuffer.allocate(512);
        // 校验CVV指令
        buff.put((byte) 0xD1);
        buff.put((byte) 0x44);
        // 写入CVKA
        buff.put(CVKA);
        // 写入CVKB
        buff.put(CVKB);
        // 写入CVV
        buff.put(CVV);
        // 写入卡号
        buff.put(cardNo);
        // 写入分隔符
        buff.put(splitSym);
        // 写入有效期
        buff.put(exprDate);
        // 写入服务商代码
        buff.put(servCode);

        // 写入完毕，翻回
        buff.flip();
        byte[] request = new byte[buff.limit()];
        buff.get(request);
        log.debug("向加密机发送的验证CVV转换报文为[{}]", Hex.encodeHexString(request));

        byte[] response = new HsmSocketTest().request(request);
        ByteBuffer res = ByteBuffer.wrap(response);
        log.info("end validate CVV");
        if (res.get() != 'A') {
            // byte[] errCode = new byte[1];
            // res.get(errCode);
            // throw new Exception("验证CVV加密机报错，异常码[" +
            // Hex.encodeHexString(errCode) + "]");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public byte[] decryPIN(byte[] pik, byte[] pinBlock, byte[] pinFormat, byte[] cardNo) throws Exception {
        log.info("starting decryPIN");
        // 使用新命令，解pin

        // FIXME 暂时写死
        ByteBuffer buff = ByteBuffer.allocate(512);
        // 转PINBLOCK指令
        buff.put((byte) 0xD1);
        buff.put((byte) 0x26);
        // PIK长度
        buff.put((byte) pik.length);
        // PIK，由LMK加密的PIK
        buff.put(pik);
        // PinFormat，固定值01
        buff.put(pinFormat);
        // PINBlock,从TPS获得
        buff.put(pinBlock);
        // 卡账号，16位
        buff.put(cardNo);
        // 写入完毕，返回
        buff.flip();
        byte[] msg = new byte[buff.limit()];
        buff.get(msg);
        log.debug("解密PIN的HEX【{}】", Hex.encodeHexString(msg));

        // System.err.println("解密PIN："+Hex.encodeHexString(msg));

        byte[] response = new HsmSocketTest().request(msg);
        ByteBuffer res = ByteBuffer.wrap(response);
        log.info("end decryPIN");
        if (res.get() != 'A') {
            byte[] errCode = new byte[1];
            res.get(errCode);
            throw new Exception("decryPIN解密PIN加密机报错，异常码[" + Hex.encodeHexString(errCode) + "]");
        } else {
            // 注意，解密后的报文为A+长度+pin；再次使用时应去除A和长度
            byte[] pin = new byte[PINBLOCK_LENGTH];
            res.get(pin);
            return pin;
        }
    }

    @Override
    public byte[] genratePINOFFSET(byte[] pik, byte[] pvk, byte[] pinBlock, byte[] pinFormat, byte[] cardNo, byte[] asciiCode, byte[] pinEffect) throws Exception {
        log.info("starting generatePINOFFSET");
        // 使用新指令产生pinOffSet

        // FIXME 暂时写死
        ByteBuffer buff = ByteBuffer.allocate(2048);
        // 产生PINOFFSET指令
        buff.put((byte) 0xD1);
        buff.put((byte) 0x4C);
        // PIK长度
        buff.put((byte) pik.length);
        // PIK，配置文件中读取
        buff.put(pik);
        // PVK长度
        buff.put((byte) pvk.length);
        // PVK，LMK加密后的密钥，配置文件中获取
        buff.put(pvk);

        // pinBlock,从TPS读取
        buff.put(pinBlock);
        // pinFormat，固定值 01
        buff.put(pinFormat);
        // 卡账号，16位卡号中，去掉前三位，去掉最后的校验位
        buff.put(cardNo);
        // 十进制表
        buff.put(asciiCode);
        // PIN有效数据000000N00000
        buff.put(pinEffect);
        // 写入完毕，返回
        buff.flip();
        byte[] msg = new byte[buff.limit()];
        buff.get(msg);
        log.debug("产生PinOffSet的HEX：【{}】", Hex.encodeHexString(msg));


        byte[] response = new HsmSocketTest().request(msg);
        ByteBuffer res = ByteBuffer.wrap(response);
        log.info("end generatePINOFFSET");
        if (res.get() != 'A') {
            byte[] errCode = new byte[1];
            res.get(errCode);
            throw new Exception("genratePINOFFSET加密机报错，异常码[" + Hex.encodeHexString(errCode) + "]");
        } else {
            byte[] pinoffset = new byte[PINOFFSET_LENGTH];
            res.get(pinoffset);
            return pinoffset;
        }
    }

    @Override
    public boolean validatePINOFFSET(byte[] pik, byte[] pvk, byte[] pinBlock, byte[] pinFormat, byte[] cardNo, byte[] asciiCode, byte[] pinEffect, byte[] pinOffSet) throws Exception, ConnectException {
        log.info("starting validatePINOFFSET");
        // 校验pinoffset

        // FIXME 暂时写死
        ByteBuffer buff = ByteBuffer.allocate(512);
        // 产生PINOFFSET指令
        buff.put((byte) 0xD1);
        buff.put((byte) 0x4D);
        // PIK长度
        buff.put((byte) pik.length);
        // PIK，配置文件中读取
        buff.put(pik);
        // PVK长度
        buff.put((byte) pvk.length);
        // PVK，LMK加密后的密钥，配置文件中获取
        buff.put(pvk);

        // pinBlock,从TPS读取
        buff.put(pinBlock);
        // pinFormat，固定值 01
        buff.put(pinFormat);
        //PinLength  固定值 6位密码
        buff.put((byte) 6);//校验PIN OFFSET-2<0xD14D>
        // 卡账号，16位卡号中，去掉前三位，去掉最后的校验位
        buff.put(cardNo);
        // 十进制表
        buff.put(asciiCode);
        // PIN有效数据000000N00000
        buff.put(pinEffect);
        // 从数据库获取的offSet
        buff.put(pinOffSet);
        // 写入完毕，返回
        buff.flip();
        byte[] msg = new byte[buff.limit()];
        buff.get(msg);
        log.debug("校验PinOffSet的HEX：【{}】", Hex.encodeHexString(msg));

        byte[] response = new HsmSocketTest().request(msg);
        ByteBuffer res = ByteBuffer.wrap(response);
        log.info("end validatePINOFFSET");
        if (res.get() != 'A') {
            byte[] errCode = new byte[1];
            res.get(errCode);
            log.debug("validatePINOFFSET加密机报错，异常码[" + Hex.encodeHexString(errCode) + "]");
            // throw new Exception("validatePINOFFSET加密机报错，异常码[" +
            // Hex.encodeHexString(errCode) + "]");
            return false;
        } else {
            // log.debug("validatePINOFFSET 验证通过");
            return true;
        }
    }

    @Override
    public RandomKey generateRandomKey(KeyType keyType, byte[] zmkIndex, int keyLength) throws Exception {
        log.debug("组装产生随机密钥加密机指令");

        // FIXME 暂时写死
        ByteBuffer buff = ByteBuffer.allocate(2048);
        // 生成随机密钥指令（ZMK加密下）
        byte[] command = {(byte) 0xD1, (byte) 0x07};
        buff.put(command);
        log.debug("指令[D107]");
        // 工作密钥长度
        buff.put((byte) keyLength);
        // 密钥类型
        buff.put(keyType.getValue());
        // ZMK INDEX
        buff.put(zmkIndex);
        // 写入完毕，翻回
        buff.flip();
        byte[] request = new byte[buff.limit()];
        buff.get(request);

        log.debug("向加密机发送的密钥转换报文为[{}]", Hex.encodeHexString(request));
        byte[] response = new HsmSocketTest().request(request);
        ByteBuffer res = ByteBuffer.wrap(response);
        log.debug("结束产生随机密钥加密机指令");
        if (res.get() != 'A') {
            byte[] errCode = new byte[1];
            res.get(errCode);
            throw new Exception("加密机报错，异常码[" + Hex.encodeHexString(errCode) + "]");
        } else {
            // 得到密钥长度
            byte wkLength = res.get();
            byte[] wkUnderLMK = new byte[wkLength];
            res.get(wkUnderLMK);
            byte[] wkUnderZMK = new byte[wkLength];
            res.get(wkUnderZMK);
            byte[] wkCheckValue = new byte[8];
            res.get(wkCheckValue);
            log.debug("加密机产生的随机工作密钥(under ZMK)[{}], check value[{}]", Hex.encodeHexString(wkUnderZMK), Hex.encodeHexString(wkCheckValue));
            RandomKey randomKey = new RandomKey(wkUnderZMK, wkCheckValue);
            return randomKey;
        }
    }

    @Override
    public byte[] transformKeyBySM4(byte[] zmkIndex, byte[] key) throws Exception {
        throw new Exception("加密机不支持该交易");
    }

    @Override
    public byte[] genrateMACBySM4(byte[] key, byte[] mab) throws Exception {
        throw new Exception("加密机不支持该交易");
    }

    @Override
    public boolean validateMACBySM4(byte[] key, byte[] mab, byte[] mac) throws Exception {
        throw new Exception("加密机不支持该交易");
    }

    @Override
    public byte[] transformPINBySM4to3DES(byte[] srcKey, byte[] destKey, byte srcFormat, byte destFormat, byte[] srcPIN, String srcAccount, String destAccount) throws Exception {
        throw new Exception("加密机不支持该交易");
    }

    @Override
    public byte[] transformPINBy3DEStoSM4(byte[] srcKey, byte[] destKey, byte srcFormat, byte destFormat, byte[] srcPIN, String srcAccount, String destAccount) throws Exception {
        throw new Exception("加密机不支持该交易");
    }

    @Override
    public RandomKey generateRandomKeyBySM4(byte[] zmkIndex, int keyLength) throws Exception {
        throw new Exception("加密机不支持该交易");
    }
}
