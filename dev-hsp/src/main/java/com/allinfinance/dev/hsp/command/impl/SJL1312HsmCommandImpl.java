package com.allinfinance.dev.hsp.command.impl;

import com.allinfinance.dev.hsp.command.HsmCommand;
import com.allinfinance.dev.hsp.exception.CodecException;
import com.allinfinance.dev.hsp.service.KeyType;
import com.allinfinance.dev.hsp.service.RandomKey;
import com.allinfinance.dev.hsp.socket.HsmSocketTest;
import com.allinfinance.dev.hsp.utils.CodecUtil;
import com.allinfinance.dev.hsp.utils.LengthType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Classname  com.allinfinance.dev.hsp.command.impl.SJL1312HsmCommandImpl
 *
 * @Description TODO
 * @Date 2021/3/25 11:41
 * @Created by ZhangYong
 */
@Slf4j
@Component
public class SJL1312HsmCommandImpl extends SJL05HsmCommandImpl {

    /**
     * PIN的长度(BySM4)
     */
    protected static final int PIN_LENGTH_SM4 = 16;

    @Override
    public byte[] transformKeyBySM4(byte[] zmkIndex, byte[] key) throws Exception {
        log.debug("组装密钥转换加密机指令(SM4)");

        // FIXME 暂时写死
        ByteBuffer buff = ByteBuffer.allocate(2048);
        // 转换密钥指令
        byte[] command = {(byte) 0xE1, (byte) 0x01};
        buff.put(command);
        log.debug("指令[E101]");
        // ZMK INDEX
        buff.put(zmkIndex);
        // 算法类型：SM4算法
        buff.put((byte) 0x04);
        // 工作密钥长度
        buff.put((byte) key.length);
        // 密钥
        buff.put(key);
        // LMK INDEX
        byte[] lmkIndex = {(byte) 0xFF, (byte) 0xFF};
        buff.put(lmkIndex);
        // 算法类型：SM4算法
        buff.put((byte) 0x04);
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
            log.debug("加密机转换后的工作密钥[{}]", Hex.encodeHexString(wk));
            return wk;
        }
    }

    @Override
    public byte[] genrateMACBySM4(byte[] key, byte[] mab) throws Exception {
        log.debug("组装生成MAC加密机指令(SM4)");

        // FIXME 暂时写死
        ByteBuffer buff = ByteBuffer.allocate(2048);
        // 生成MAC指令
        byte[] command = {(byte) 0xE1, (byte) 0x10};
        buff.put(command);
        log.debug("指令[E110]");
        // 主密钥类型：LMK
        buff.put((byte) 0x00);
        // LMK INDEX
        byte[] lmkIndex = {(byte) 0xFF, (byte) 0xFF};
        buff.put(lmkIndex);
        // MAK长度
        buff.put((byte) key.length);
        // MAC算法:银联定义
        buff.put((byte) 0x05);
        // MAK
        buff.put(key);
        log.debug("MAK[{}】", Hex.encodeHexString(key));
        // 初始向量
        byte[] initialVector = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
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
    public boolean validateMACBySM4(byte[] key, byte[] mab, byte[] mac) throws Exception {
        log.debug("组装验证MAC加密机指令(SM4)");

        // FIXME 暂时写死
        ByteBuffer buff = ByteBuffer.allocate(2048);
        // 验证MAC指令
        byte[] command = {(byte) 0xE1, (byte) 0x1A};
        buff.put(command);
        log.debug("指令[E11A]");
        // 主密钥类型：LMK
        buff.put((byte) 0x00);
        // LMK INDEX
        byte[] lmkIndex = {(byte) 0xFF, (byte) 0xFF};
        buff.put(lmkIndex);
        // MAK长度
        buff.put((byte) key.length);
        // MAC算法:银联定义
        buff.put((byte) 0x05);
        // MAK
        buff.put(key);
        log.debug("MAK[{}]", Hex.encodeHexString(key));
        // 初始向量
        byte[] initialVector = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        buff.put(initialVector);
        log.debug("MAC[{}]", Hex.encodeHexString(mac));
        // MAC(8位)
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
    public byte[] transformPINBySM4to3DES(byte[] srcKey, byte[] destKey, byte srcFormat, byte destFormat, byte[] srcPIN, String srcAccount, String destAccount) throws Exception {
        log.debug("组装转PIN加密机指令SM4转3DES");

        // FIXME 暂时写死
        ByteBuffer buff = ByteBuffer.allocate(2048);
        // 转PIN指令
        byte[] command = {(byte) 0xE1, (byte) 0x16};
        buff.put(command);
        log.debug("指令[E116]");
        // LMK INDEX
        byte[] lmkIndex = {(byte) 0xFF, (byte) 0xFF};
        byte[] changeLmkIndex = {(byte) 0xDD, (byte) 0xDD};
        buff.put(lmkIndex);
        // 算法类型：SM4算法
        buff.put((byte) 0x04);
        // PIK1长度
        buff.put((byte) srcKey.length);
        // PIK1
        buff.put(srcKey);
        log.debug("PIK1[" + Hex.encodeHexString(srcKey) + "]");
        // PinBlock1算法保留使用
        buff.put((byte) 0x00);
        // PIN密文
        buff.put(srcPIN);
        // 源账号
        // SM4截取卡号4~15位 前补00 (10个)转为bcd格式
        // 初始向量
        byte[] fill = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00};
        buff.put(fill);
        try {
            buff.put(Hex.decodeHex(srcAccount.substring(3, 15).toCharArray()));
        } catch (DecoderException e) {
            log.warn("绝对不应出现的异常", e);
            return null;
        }
        // LMK INDEX
        buff.put(changeLmkIndex);
        // 算法类型：3DES 128位
        buff.put((byte) 0x01);
        // PIK2长度
        buff.put((byte) destKey.length);
        // PIK2
        buff.put(destKey);
        log.debug("PIK2[" + Hex.encodeHexString(destKey) + "]");
        // PinBlock2算法保留使用
        buff.put((byte) 0x00);
        // 目标账号
        //3des截取卡号4~15位 前补00 (2个)转为bcd格式
        buff.put((byte) 0x00);
        buff.put((byte) 0x00);
        try {
            buff.put(Hex.decodeHex(destAccount.substring(3, 15).toCharArray()));
        } catch (DecoderException e) {
            log.warn("绝对不应出现的异常", e);
            return null;
        }
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
    public byte[] transformPINBy3DEStoSM4(byte[] srcKey, byte[] destKey, byte srcFormat, byte destFormat, byte[] srcPIN, String srcAccount, String destAccount) throws Exception {
        log.debug("组装转PIN加密机指令3DES转SM4");

        // FIXME 暂时写死
        ByteBuffer buff = ByteBuffer.allocate(2048);
        // 转PIN指令
        byte[] command = {(byte) 0xE1, (byte) 0x13};
        buff.put(command);
        log.debug("指令[E113]");
        // LMK INDEX
        byte[] lmkIndex = {(byte) 0xFF, (byte) 0xFF};
        byte[] changeLmkIndex = {(byte) 0xFF, (byte) 0xFF};
        buff.put(changeLmkIndex);
        // 算法类型：3DES 128位
        buff.put((byte) 0x01);
        // PIK1长度
        buff.put((byte) srcKey.length);
        // PIK1
        buff.put(srcKey);
        log.debug("PIK1[" + Hex.encodeHexString(srcKey) + "]");
        // PinBlock1算法保留使用
        buff.put((byte) 0x00);
        // PIN密文
        buff.put(srcPIN);
        // 源账号
        //3des截取卡号4~15位 前补00 (2个)转为bcd格式
        buff.put((byte) 0x00);
        buff.put((byte) 0x00);
        try {
            buff.put(Hex.decodeHex(srcAccount.substring(3, 15).toCharArray()));
        } catch (DecoderException e) {
            log.warn("绝对不应出现的异常", e);
            return null;
        }
        // LMK INDEX
        buff.put(lmkIndex);
        // 算法类型：SM4
        buff.put((byte) 0x04);
        // PIK2长度
        buff.put((byte) destKey.length);
        // PIK2
        buff.put(destKey);
        log.debug("PIK2[" + Hex.encodeHexString(destKey) + "]");
        // PinBlock2算法保留使用
        buff.put((byte) 0x00);
        // 目标账号
        // SM4截取卡号4~15位 前补00 (10个)转为bcd格式
        // 初始向量
        byte[] fill = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00};
        buff.put(fill);
        try {
            buff.put(Hex.decodeHex(destAccount.substring(3, 15).toCharArray()));
        } catch (DecoderException e) {
            log.warn("绝对不应出现的异常", e);
            return null;
        }
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
            byte[] pin = new byte[PIN_LENGTH_SM4];
            res.get(pin);
            log.debug("加密机计算得到的PIN[{}]", Hex.encodeHexString(pin));
            return pin;
        }
    }

    @Override
    public RandomKey generateRandomKeyBySM4(byte[] zmkIndex, int keyLength) throws Exception {
        log.debug("组装产生随机密钥加密机指令");

        // FIXME 暂时写死
        ByteBuffer buff = ByteBuffer.allocate(2048);
        // 生成随机密钥指令（ZMK加密下）
        byte[] command = {(byte) 0xE1, (byte) 0x04};
        buff.put(command);
        log.debug("指令[E104]");
        // ZMK INDEX
        buff.put(zmkIndex);
        // 算法类型：SM4算法
        buff.put((byte) 0x04);
        // 工作密钥长度
        buff.put((byte) keyLength);
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
            byte[] wkUnderZMK = new byte[wkLength];
            res.get(wkUnderZMK);
            byte[] wkCheckValue = new byte[16];
            res.get(wkCheckValue);
            log.debug("加密机产生的随机工作密钥(under ZMK)[{}], check value[{}]", Hex.encodeHexString(wkUnderZMK), Hex.encodeHexString(wkCheckValue));
            RandomKey randomKey = new RandomKey(wkUnderZMK, wkCheckValue);
            return randomKey;
        }
    }

    //    bd5a435ba64963bd
    public static void main(String[] args) throws Exception {
        HsmCommand command = new SJL1312HsmCommandImpl();
        byte[] zmkIndex = {0x00, 0x6F};
        byte[] key = "1111111111111111".getBytes(StandardCharsets.UTF_8);
        byte[] bytes = command.transformKey(KeyType.MAK, zmkIndex, key);
        log.info("加密后的MAK");

        byte[] mab = {0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38};
        byte[] mac = command.genrateMAC(key, mab);

        log.info("mac....");


        boolean b = command.validateMAC(bytes, mab, "d37f33ec845efafd".getBytes());
        log.info("validate mac...");
    }
}
