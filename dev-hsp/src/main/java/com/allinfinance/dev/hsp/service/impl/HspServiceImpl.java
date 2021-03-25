package com.allinfinance.dev.hsp.service.impl;

import com.allinfinance.dev.hsp.cache.KeyCache;
import com.allinfinance.dev.hsp.command.HsmCommand;
import com.allinfinance.dev.hsp.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.rmi.ConnectException;
import java.util.Arrays;

/**
 * Classname  com.allinfinance.dev.hsp.service.impl.HspServiceImpl
 *
 * @Description TODO
 * @Date 2021/3/24 16:51
 * @Created by ZhangYong
 */
@Slf4j
@Service("hspService")
public class HspServiceImpl implements HspService {

    @Autowired
    private HsmCommand hsmCommand;
    @Autowired
    private KeyCache keyCache;

    // CVV使用的参数
//    @Value("#{env.splitSym}")
    private String splitSym;
//    @Value("#{env.cvv2ServCode}")
    private String cvv2ServCode;
//    @Value("#{env.pinFormat}")
    private String pinFormat;

//    @Value("#{env.asciiCode}")
    private String asciiCode;

    public boolean validateMAC(String keyIndex, byte[] mac, byte[] mab) {
        try {
            // 根据密钥索引获得密钥
            String macKey = keyCache.get(keyIndex).getKeyValue();
            // 截取MAC的前4位
            byte[] bcdMac = Hex.decodeHex(new String(mac).toCharArray());
            return hsmCommand.validateMAC(Hex.decodeHex(macKey.toCharArray()), mab, bcdMac);
        } catch (DecoderException e) {
            log.warn("绝对不应出现的异常", e);
            return false;
        } catch (ConnectException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateMAC(String keyIndex, byte[] macKey, byte[] mac, byte[] mab) {
        try {
            // 根据密钥索引获得zmkIndex
            String zmkIndex = keyCache.get(keyIndex).getZmkIndex();
            // 将密钥转换为LMK加密的密钥
            byte[] lmkMac = hsmCommand.transformKey(KeyType.MAK, Hex.decodeHex(zmkIndex.toCharArray()), macKey);
            // 截取MAK重置交易的待校验MAC
            byte[] macHead = Arrays.copyOf(mac, 4);
            return hsmCommand.validateMAC(lmkMac, mab, macHead);
        } catch (DecoderException e) {
            log.warn("绝对不应出现的异常", e);
            return false;
        } catch (ConnectException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] genrateMAC(String keyIndex, byte[] mab) {
        try {
            // 根据密钥索引获得密钥
            String mak = keyCache.get(keyIndex).getKeyValue();
            // 生成MAC
            return hsmCommand.genrateMAC(Hex.decodeHex(mak.toCharArray()), mab);
        } catch (DecoderException e) {
            log.warn("绝对不应出现的异常，检查代码", e);
            return null;
        } catch (ConnectException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] genrateMAC(String keyIndex, byte[] macKey, byte[] mab) {
        try {
            // 根据密钥索引获得ZMK索引
            String zmkIndex = keyCache.get(keyIndex).getZmkIndex();
            // 将密钥转换为LMK加密的密钥
            byte[] lmkMac = hsmCommand.transformKey(KeyType.MAK, Hex.decodeHex(zmkIndex.toCharArray()), macKey);
            // 根据LMK加密的MAK生成MAC
            byte[] mac = hsmCommand.genrateMAC(lmkMac, mab);
            return mac;
        } catch (DecoderException e) {
            log.warn("绝对不应出现的异常，检查密钥索引的长度，应为两位数", e);
            return null;
        } catch (ConnectException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] transformPIN(String srcKeyIndex, String destKeyIndex, byte srcFormat, byte destFormat, byte[] srcPIN, String srcAccount, String destAccount) {
        // 根据密钥索引获得源密钥
        String srcPINKey = keyCache.get(srcKeyIndex).getKeyValue();
        // 根据密钥索引获得目标密钥
        String destPINKey = keyCache.get(destKeyIndex).getKeyValue();
        try {
            return hsmCommand.transformPIN(Hex.decodeHex(srcPINKey.toCharArray()), Hex.decodeHex(destPINKey.toCharArray()), srcFormat, destFormat, srcPIN, srcAccount, destAccount);
        } catch (DecoderException e) {
            log.warn("绝对不应出现的异常，检查密钥索引的长度，应为两位数", e);
            return null;
        } catch (ConnectException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void registerKey(String keyIndex, KeyType keyType, byte[] key) {
        try {
            // 根据密钥索引获得ZMK索引
            String zmkIndex = keyCache.get(keyIndex).getZmkIndex();
            // 将密钥转换为LMK加密的密钥
            byte[] wk = hsmCommand.transformKey(keyType, Hex.decodeHex(zmkIndex.toCharArray()), key);
            // 将KEY放入缓存
            String value = Hex.encodeHexString(wk);
            keyCache.put(keyIndex, value);
            log.debug("[{}]的密钥更新为[{}]", keyIndex, value);
            // 向广播队列发送消息，通知密钥列表更新
            // FIXME: 2021/3/25 广播策略的实现：由于不想引入消息队列，故而在此处采取的方式为rpc consumer调用的方式来做，provider需提供密钥更新的服务供consumer调用
//            refreshTemplate.convertAndSend("a", keyIndex);// 对列名无所谓
        } catch (DecoderException e) {
            log.warn("绝对不应出现的异常，检查密钥索引的长度，应为两位数", e);
            return;
        } catch (ConnectException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String genrateCVV(String cardNo, String exprDate, String servCode, EncryptIndex encry) {
        // 产生CVV
        String CVV = "";

        log.debug("产生CVV卡号：{}", cardNo);
        log.debug("产生CVV有效期：{}", exprDate);
        log.debug("产生CVV服务码：{}", servCode);
        log.debug("产生CVV的encry对象CVKA：" + keyCache.get(encry.getCvkAIndex()).getKeyValue() + ":CVKB:" + keyCache.get(encry.getCvkBIndex()).getKeyValue() + ":PVK:"
                + keyCache.get(encry.getPvkIndex()).getKeyValue() + ":PIK:" + keyCache.get(encry.getPikIndex()).getKeyValue());

        try {

            String cvka = keyCache.get(encry.getCvkAIndex()).getKeyValue();
            String cvkb = keyCache.get(encry.getCvkBIndex()).getKeyValue();

            // byte[] cvvByte =
            // hsmCommand.genrateCVV(Hex.decodeHex(CVKA.toCharArray()),
            // Hex.decodeHex(CVKB.toCharArray()), cardNo.getBytes(),
            // splitSym.getBytes(), exprDate.getBytes(),
            // servCode.getBytes());

            byte[] cvvByte = hsmCommand.genrateCVV(Hex.decodeHex(cvka.toCharArray()), Hex.decodeHex(cvkb.toCharArray()), cardNo.getBytes(), splitSym.getBytes(), exprDate.getBytes(),
                    servCode.getBytes());
            CVV = new String(cvvByte);
            log.debug("最终CVV：" + CVV);
            return CVV;
        } catch (DecoderException e) {
            log.warn("产生CVV出现HEX异常，检查CVK相关配置，", e);
            return null;
        } catch (ConnectException e) {
            log.warn("connection出现异常", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.warn("加密机出现异常", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validateCVV(String cardNo, String exprDate, String servCode, String cvv, EncryptIndex encry) {
        // 组织验证CVV的报文，并传入加密机进行验证

        log.debug("验证CVV卡号：" + cardNo);
        log.debug("验证CVV有效期：" + exprDate);
        log.debug("验证CVV服务码：" + servCode);
        log.debug("验证CVV值：" + cvv);
        log.debug("验证CVV的encry对象CVKA：" + keyCache.get(encry.getCvkAIndex()).getKeyValue() + ":CVKB:" + keyCache.get(encry.getCvkBIndex()).getKeyValue() + ":PVK:"
                + keyCache.get(encry.getPvkIndex()).getKeyValue() + ":PIK:" + keyCache.get(encry.getPikIndex()).getKeyValue());

        try {
            String cvkA = keyCache.get(encry.getCvkAIndex()).getKeyValue();
            String cvkB = keyCache.get(encry.getCvkBIndex()).getKeyValue();

            return hsmCommand.validateCVV(Hex.decodeHex(cvkA.toCharArray()), Hex.decodeHex(cvkB.toCharArray()), cvv.getBytes(),

                    cardNo.getBytes(), splitSym.getBytes(), exprDate.getBytes(), servCode.getBytes());

        } catch (DecoderException e) {
            log.warn("校验CVV报文转码出现异常，检查CVK相关配置，", e);
            return false;
        } catch (ConnectException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public String genrateCVV2(String cardNo, String exprDate, EncryptIndex encry) {
        log.debug("生成CVV2开始");
        // 产生CVV2
        String CVV2 = "";

        log.debug("产生CVV2卡号：" + cardNo);
        log.debug("产生CVV2有效期：" + exprDate);
        // log.debug("产生CVV2服务码："+servCode);
        log.debug("产生CVV2的encry对象CVKA：" + keyCache.get(encry.getCvkAIndex()).getKeyValue() + ":CVKB:" + keyCache.get(encry.getCvkBIndex()).getKeyValue() + ":PVK:"
                + keyCache.get(encry.getPvkIndex()).getKeyValue() + ":PIK:" + keyCache.get(encry.getPikIndex()).getKeyValue());

        try {
            String cvkA = keyCache.get(encry.getCvkAIndex()).getKeyValue();
            String cvkB = keyCache.get(encry.getCvkBIndex()).getKeyValue();

            byte[] cvv2Byte = hsmCommand.genrateCVV(Hex.decodeHex(cvkA.toCharArray()), Hex.decodeHex(cvkB.toCharArray()),

                    cardNo.getBytes(), splitSym.getBytes(), exprDate.getBytes(), cvv2ServCode.getBytes());

            CVV2 = new String(cvv2Byte);
            log.debug("最终CVV2：" + CVV2);
            // System.err.println("最终CVV2："+CVV2);
            log.debug("生成CVV2结束");
            return CVV2;
        } catch (DecoderException e) {
            log.warn("产生CVV2转码出现异常，检查CVK相关配置，", e);
            return null;
        } catch (ConnectException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validateCVV2(String cardNo, String exprDate, String cvv2, EncryptIndex encry) {
        // 组织验证CVV2的报文，并传入加密机进行验证

        try {
            String cvkA = keyCache.get(encry.getCvkAIndex()).getKeyValue();
            String cvkB = keyCache.get(encry.getCvkBIndex()).getKeyValue();

            return hsmCommand.validateCVV(Hex.decodeHex(cvkA.toCharArray()), Hex.decodeHex(cvkB.toCharArray()), cvv2.getBytes(),

                    cardNo.getBytes(), splitSym.getBytes(), exprDate.getBytes(), cvv2ServCode.getBytes());

        } catch (DecoderException e) {
            log.warn("校验CVV2出现异常，检查CVK相关配置，", e);
            return false;
        } catch (ConnectException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String genratePINOFFSET(String pinBlock, String cardNo, EncryptIndex encry) {

        String pinOffSet = "";
        String pinEff = cardNo.substring(3, 14) + "N";// 计算pinOffSet的有效数据
        log.debug("pinEFF:" + pinEff);
        log.debug("产生pinOffSet卡号：" + cardNo);
        log.debug("产生pinOffSet pinBlock：" + pinBlock);
        log.debug("产生pinOffSet的encry对象CVKA：" + keyCache.get(encry.getCvkAIndex()).getKeyValue() + ":CVKB:" + keyCache.get(encry.getCvkBIndex()).getKeyValue() + ":PVK:"
                + keyCache.get(encry.getPvkIndex()).getKeyValue() + ":PIK:" + keyCache.get(encry.getPikIndex()).getKeyValue());

        byte[] pinOffByte = null;

        // 产生PINOFFSET
        try {
            String pik = keyCache.get(encry.getPikIndex()).getKeyValue();
            System.err.println(pik);
            String pvk = keyCache.get(encry.getPvkIndex()).getKeyValue();

            pinOffByte = hsmCommand.genratePINOFFSET(Hex.decodeHex(pik.toCharArray()),// PIK
                    Hex.decodeHex(pvk.toCharArray()),// PVK
                    Hex.decodeHex(pinBlock.toCharArray()),// PINBLOCK从TPS传输过来
                    Hex.decodeHex(pinFormat.toCharArray()),
                    cardNo.substring(3, 15).getBytes(),// 卡账号，去掉前三位，去掉最后一位
                    asciiCode.getBytes(),
                    // pinEffect.getBytes()
                    pinEff.getBytes());

        } catch (ConnectException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        pinOffSet = new String(pinOffByte);
        log.debug("产生的pinOffSet：" + pinOffSet);
        System.err.println("产生的pinOffSet：" + pinOffSet);
        return pinOffSet;
    }

    @Override
    public boolean validatePINOFFSET(String pinBlock, String cardNo, String pinOffSet, EncryptIndex encry) {
        // 组织验证pinOffSet的报文，并传入加密机进行验证

        log.debug("验证pinOffSet卡号：" + cardNo);
        log.debug("验证pinOffSet pinBlock：" + pinBlock);
        log.debug("验证pinOffSet pinOffSet：" + pinOffSet);
        log.debug("验证pinOffSet的encry对象CVKA：" + keyCache.get(encry.getCvkAIndex()).getKeyValue() + ":CVKB:" + keyCache.get(encry.getCvkBIndex()).getKeyValue() + ":PVK:"
                + keyCache.get(encry.getPvkIndex()).getKeyValue() + ":PIK:" + keyCache.get(encry.getPikIndex()).getKeyValue());
        boolean validPin = false;
        log.debug("校验pinOffSet前的布尔值：" + validPin);
        try {

            String pik = keyCache.get(encry.getPikIndex()).getKeyValue();
            String pvk = keyCache.get(encry.getPvkIndex()).getKeyValue();
            String pinEff = cardNo.substring(3, 14) + "N";// 计算pinOffSet的有效数据
            log.debug("pinEFF:" + pinEff);

            // return
            // hsmCommand.validatePINOFFSET(Hex.decodeHex(pik.toCharArray()),//
            // PIK
            // Hex.decodeHex(pvk.toCharArray()),// PVK，加密后的密钥
            // Hex.decodeHex(pinBlock.toCharArray()),// pinBlock，从TPS直接获得
            //
            // Hex.decodeHex(pinFormat.toCharArray()), cardNo.substring(3,
            // 15).getBytes(),// 卡账号，去掉前三位，去掉最后一位
            // asciiCode.getBytes(),// ascii码表
            // pinEffect.getBytes(), pinOffSet.getBytes());// 从数据库获取的pinOffSet

            validPin = hsmCommand.validatePINOFFSET(Hex.decodeHex(pik.toCharArray()),// PIK
                    Hex.decodeHex(pvk.toCharArray()),// PVK，加密后的密钥
                    Hex.decodeHex(pinBlock.toCharArray()),// pinBlock，从TPS直接获得

                    Hex.decodeHex(pinFormat.toCharArray()), cardNo.substring(3, 15).getBytes(),// 卡账号，去掉前三位，去掉最后一位
                    asciiCode.getBytes(),// ascii码表
                    pinEff.getBytes(), pinOffSet.getBytes());// 从数据库获取的pinOffSet
            log.debug("校验pinOffSet后的布尔值：" + validPin);

        } catch (DecoderException e) {
            log.warn("校验pinOffSet转码HEX说出现异常，检查pinOffSet相关配置，", e);
            return false;
        } catch (ConnectException e) {
            log.warn("校验pinOffSet RMI连接出现异常，检查pinOffSet相关配置，", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.warn("校验pinOffSet加密机出现异常，检查pinOffSet相关配置，", e);
            throw new RuntimeException(e);
        }
        log.debug("校验pinOffSet最终返回的布尔值：" + validPin);
        return validPin;
    }

    public static void main(String[] args) throws ConnectException, Exception {
        ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("/service-context.xml");
        HsmCommand command = ctx.getBean(HsmCommand.class);
        if (args.length < 2) {
            System.out.println("输入参数格式有误：输入参数格式：<MAK|PIK> <明文密钥HEX字符串>");
            ctx.close();
            return;
        }
        KeyType keyType;
        byte[] key;
        try {
            keyType = KeyType.valueOf(args[0]);
            key = Hex.decodeHex(args[1].toCharArray());
        } catch (Exception e) {
            System.out.println("输入参数格式有误：输入参数格式：<MAK|PIK> <明文密钥HEX字符串>");
            ctx.close();
            return;
        }
        command.transformLmkKey(keyType, key);
        ctx.close();
        System.exit(0);
    }

    public RandomKey generateRandomKey(String keyIndex, KeyType keyType,
                                       int keyLength) {

        try {
            // 根据密钥索引获得ZMK索引
            String zmkIndex = keyCache.get(keyIndex).getZmkIndex();
            // 生成随机密钥（ZMK加密）
            return hsmCommand.generateRandomKey(keyType, Hex.decodeHex(zmkIndex.toCharArray()), keyLength);
        } catch (DecoderException e) {
            log.warn("绝对不应出现的异常，检查密钥索引的长度，应为两位数", e);
            return null;
        } catch (ConnectException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean validateMACByAlgorithm(String keyIndex, byte[] mac,
                                          byte[] mab, EncryptAlgorithm algorithm) {
        if (algorithm == EncryptAlgorithm.DES3) {
            return validateMAC(keyIndex, mac, mab);
        } else if (algorithm == EncryptAlgorithm.SM4) {
            try {
                // 根据密钥索引获得密钥
                log.debug("keyIndex" + keyIndex);
                String macKey = keyCache.get(keyIndex).getKeyValue();
                return hsmCommand.validateMACBySM4(Hex.decodeHex(macKey.toCharArray()), mab, mac);
            } catch (DecoderException e) {
                log.warn("绝对不应出现的异常", e);
                return false;
            } catch (ConnectException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("不支持的加密算法：" + algorithm.toString());
        }
    }

    @Override
    public boolean validateMACByAlgorithm(String keyIndex, byte[] macKey,
                                          byte[] mac, byte[] mab, EncryptAlgorithm algorithm) {
        if (algorithm == EncryptAlgorithm.DES3) {
            return validateMAC(keyIndex, macKey, mac, mab);
        } else if (algorithm == EncryptAlgorithm.SM4) {
            try {
                // 根据密钥索引获得zmkIndex
                String zmkIndex = keyCache.get(keyIndex).getZmkIndex();
                // 将密钥转换为LMK加密的密钥
                byte[] lmkMac = hsmCommand.transformKeyBySM4(Hex.decodeHex(zmkIndex.toCharArray()), macKey);
                // 取mac前4位并展开为8位
                byte[] macHead = Hex.encodeHexString(Arrays.copyOf(mac, 4)).getBytes();
                return hsmCommand.validateMACBySM4(lmkMac, mab, macHead);
            } catch (DecoderException e) {
                log.warn("绝对不应出现的异常", e);
                return false;
            } catch (ConnectException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("不支持的加密算法：" + algorithm.toString());
        }
    }

    @Override
    public byte[] genrateMACByAlgorithm(String keyIndex, byte[] mab,
                                        EncryptAlgorithm algorithm) {
        if (algorithm == EncryptAlgorithm.DES3) {
            return genrateMAC(keyIndex, mab);
        } else if (algorithm == EncryptAlgorithm.SM4) {
            try {
                // 根据密钥索引获得密钥
                String mak = keyCache.get(keyIndex).getKeyValue();
                // 生成MAC
                return hsmCommand.genrateMACBySM4(Hex.decodeHex(mak.toCharArray()), mab);
            } catch (DecoderException e) {
                log.warn("绝对不应出现的异常，检查代码", e);
                return null;
            } catch (ConnectException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("不支持的加密算法：" + algorithm.toString());
        }
    }

    @Override
    public byte[] genrateMACByAlgorithm(String keyIndex, byte[] macKey,
                                        byte[] mab, EncryptAlgorithm algorithm) {
        if (algorithm == EncryptAlgorithm.DES3) {
            return genrateMAC(keyIndex, macKey, mab);
        } else if (algorithm == EncryptAlgorithm.SM4) {
            try {
                // 根据密钥索引获得ZMK索引
                String zmkIndex = keyCache.get(keyIndex).getZmkIndex();
                // 将密钥转换为LMK加密的密钥
                byte[] lmkMac = hsmCommand.transformKeyBySM4(Hex.decodeHex(zmkIndex.toCharArray()), macKey);
                // 根据LMK加密的MAK生成MAC
                byte[] mac = hsmCommand.genrateMACBySM4(lmkMac, mab);
                return mac;
            } catch (DecoderException e) {
                log.warn("绝对不应出现的异常，检查密钥索引的长度，应为两位数", e);
                return null;
            } catch (ConnectException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("不支持的加密算法：" + algorithm.toString());
        }
    }

    @Override
    public byte[] transformPINByAlgorithm(String srcKeyIndex,
                                          String destKeyIndex, byte srcFormat, byte destFormat,
                                          byte[] srcPIN, String srcAccount, String destAccount,
                                          EncryptAlgorithm from, EncryptAlgorithm to) {
        if (from == EncryptAlgorithm.DES3 && to == EncryptAlgorithm.DES3) {
            return transformPIN(srcKeyIndex, destKeyIndex, srcFormat, destFormat,
                    srcPIN, srcAccount, destAccount);
        } else if (from == EncryptAlgorithm.SM4 && to == EncryptAlgorithm.DES3) {
            // 根据密钥索引获得源密钥
            String srcPINKey = keyCache.get(srcKeyIndex).getKeyValue();
            // 根据密钥索引获得目标密钥
            String destPINKey = keyCache.get(destKeyIndex).getKeyValue();
            try {
                return hsmCommand.transformPINBySM4to3DES(Hex.decodeHex(srcPINKey.toCharArray()), Hex.decodeHex(destPINKey.toCharArray()), srcFormat, destFormat, srcPIN, srcAccount, destAccount);
            } catch (DecoderException e) {
                log.warn("绝对不应出现的异常，检查密钥索引的长度，应为两位数", e);
                return null;
            } catch (ConnectException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (from == EncryptAlgorithm.DES3 && to == EncryptAlgorithm.SM4) {
            // 根据密钥索引获得源密钥
            String srcPINKey = keyCache.get(srcKeyIndex).getKeyValue();
            // 根据密钥索引获得目标密钥
            String destPINKey = keyCache.get(destKeyIndex).getKeyValue();
            try {
                return hsmCommand.transformPINBy3DEStoSM4(Hex.decodeHex(srcPINKey.toCharArray()), Hex.decodeHex(destPINKey.toCharArray()), srcFormat, destFormat, srcPIN, srcAccount, destAccount);
            } catch (DecoderException e) {
                log.warn("绝对不应出现的异常，检查密钥索引的长度，应为两位数", e);
                return null;
            } catch (ConnectException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("暂不不支持的PIN转换(SM4转SM4)");
        }
    }

    @Override
    public void registerKeyByAlgorithm(String keyIndex, KeyType keyType,
                                       byte[] key, EncryptAlgorithm algorithm) {
        if (algorithm == EncryptAlgorithm.DES3) {
            registerKey(keyIndex, keyType, key);
        } else if (algorithm == EncryptAlgorithm.SM4) {
            try {
                // 根据密钥索引获得ZMK索引
                String zmkIndex = keyCache.get(keyIndex).getZmkIndex();
                // 将密钥转换为LMK加密的密钥
                byte[] wk = hsmCommand.transformKeyBySM4(Hex.decodeHex(zmkIndex.toCharArray()), key);
                // 将KEY放入缓存
                String value = Hex.encodeHexString(wk);
                keyCache.put(keyIndex, value);
                log.debug("[" + keyIndex + "]的密钥更新为[" + value + "]");
                // 向广播队列发送消息，通知密钥列表更新
                // FIXME: 2021/3/25 广播策略的实现：由于不想引入消息队列，故而在此处采取的方式为rpc consumer调用的方式来做，provider需提供密钥更新的服务供consumer调用
//                refreshTemplate.convertAndSend("a", keyIndex);// 对列名无所谓
            } catch (DecoderException e) {
                log.warn("绝对不应出现的异常，检查密钥索引的长度，应为两位数", e);
                return;
            } catch (ConnectException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("不支持的加密算法：" + algorithm.toString());
        }

    }

    @Override
    public RandomKey generateRandomKeyByAlgorithm(String keyIndex,
                                                  KeyType keyType, int keyLength, EncryptAlgorithm algorithm) {
        if (algorithm == EncryptAlgorithm.DES3) {
            return generateRandomKey(keyIndex, keyType, keyLength);
        } else if (algorithm == EncryptAlgorithm.SM4) {
            try {
                // 根据密钥索引获得ZMK索引
                String zmkIndex = keyCache.get(keyIndex).getZmkIndex();
                // 生成随机密钥（ZMK加密）
                return hsmCommand.generateRandomKeyBySM4(Hex.decodeHex(zmkIndex.toCharArray()), keyLength);
            } catch (DecoderException e) {
                log.warn("绝对不应出现的异常，检查密钥索引的长度，应为两位数", e);
                return null;
            } catch (ConnectException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("不支持的加密算法：" + algorithm.toString());
        }
    }
}
