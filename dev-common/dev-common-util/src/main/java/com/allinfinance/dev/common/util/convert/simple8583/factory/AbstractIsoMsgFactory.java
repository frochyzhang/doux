package com.allinfinance.dev.common.util.convert.simple8583.factory;


import com.allinfinance.dev.common.util.convert.simple8583.key.SimpleConstants;
import com.allinfinance.dev.common.util.convert.simple8583.model.BitMap;
import com.allinfinance.dev.common.util.convert.simple8583.model.IsoField;
import com.allinfinance.dev.common.util.convert.simple8583.model.IsoPackage;
import com.allinfinance.dev.common.util.convert.simple8583.util.EncodeUtil;
import com.allinfinance.dev.common.util.convert.simple8583.util.SimpleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报文组装抽象类.
 * </p>
 */
public abstract class AbstractIsoMsgFactory {

    protected String macKey;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected AbstractIsoMsgFactory() {

    }

    // 入口和出口
    public byte[] pack(Map<String, String> dataMap, final IsoPackage pack) throws IOException, ClassNotFoundException {
        //43域做特殊处理：交易信息中的商户名称采用汉字，该域中涉及到的中文字符编码规则需符合GB18030－2000
//        if(dataMap.containsKey("43") || dataMap.containsKey("57") || dataMap.containsKey("61") || dataMap.containsKey("104")){
//            //TODO 43域做特殊处理 该域中涉及到的中文字符编码规则需符合GB18030－2000
//        }

        // 深度拷贝，对拷贝后的对象进行操作，
        IsoPackage packClone = pack.deepClone();

        List<Integer> dataFieldList = new ArrayList<Integer>(dataMap.size());
        for (String key : dataMap.keySet()) {
            IsoField field = packClone.getIsoField(key);
            if (field == null) {
                continue;
            }
            field.setValue(dataMap.get(key));
            // 数据域
            if (SimpleUtil.isNumeric(key)) {
                int val = Integer.valueOf(key);
                if (packClone.isBit64() && val > 64) {
                    // 设置位非64位图模式，即128模式
                    packClone.setBit64(false);
                    // 将bitMap第一位置为1，表示这个数据域为128位长
                    dataFieldList.add(1);
                }
                dataFieldList.add(val);
            }
        }
        // 生成BitMap
        BitMap bitMap = null;
        if (packClone.isBit64()) {
            bitMap = new BitMap(64);
        } else {
            bitMap = new BitMap(128);
        }
        byte[] bitMapByte = bitMap.addBits(dataFieldList);
        // 设置BitMap的值
        packClone.getIsoField(SimpleConstants.BIT_MAP).setByteValue(bitMapByte);
        // 将数组合并
        return merge(packClone);
    }

    /**
     * 将返回信息拆成Map返回
     *
     * @param bts
     * @param pack
     * @return
     * @throws Exception
     */
    public Map<String, String> unpack(byte[] bts, final IsoPackage pack) throws Exception {
        if (pack == null || pack.size() == 0) {
            throw new IllegalArgumentException("配置为空，请检查IsoPackage是否为空");
        }
        Map<String, String> returnMap = new HashMap<>(16);
        // 起判断的作用
        int offset = 0;
        // 深度拷贝
        IsoPackage target = pack.deepClone();
        // 获取到bitMap
        boolean hasBitMap = false;
        BitMap bitMap = null;
        for (IsoField field : target) {

            if (field.isAppData()) {
                if (hasBitMap) {
                    int index = Integer.valueOf(field.getId());
                    if (index == 1) {
                        // 第一位不处理，只是标志位
                        continue;
                    }
                    if (bitMap.getBit(index - 1) == 1) {
                        offset += subByte(bts, offset, field);
                        returnMap.put(field.getId(), field.getValue());
                    }
                }
            } else {
                offset += subByte(bts, offset, field);
                returnMap.put(field.getId(), field.getValue());
                if (field.getId().equalsIgnoreCase(SimpleConstants.BIT_MAP)) {
                    hasBitMap = true;
                    bitMap = BitMap.addBits(field.getByteValue());
                }
            }
        }
        // TODO MAC校验
        // macValidate(pack,returnMap);
        return returnMap;
    }

    private int subByte(byte[] bts, int offset, IsoField field) throws UnsupportedEncodingException {
        byte[] val = null;
        int length = field.getLength();
        switch (field.getIsoType()) {
            case NUMERIC:
            case CHAR:
            case BINARY:
                if (field.getId().equals(SimpleConstants.BIT_MAP)) {
                    if (bts[offset] >> 15 == 0) {
                        length = 8;
                    }
                }
                val = new byte[length];
                System.arraycopy(bts, offset, val, 0, length);
                break;
            case LLVAR_NUMERIC:
                byte[] llvarNumLen = new byte[2];
                llvarNumLen[0] = bts[offset];
                llvarNumLen[1] = bts[offset + 1];
                // LLVAR_NUMERIC前面的报文域长度是数字长度
                int firstNumLen = Integer.valueOf(new String(llvarNumLen));
                val = new byte[firstNumLen];
                System.arraycopy(bts, offset + 2, val, 0, firstNumLen);
                length = 2 + firstNumLen;
                break;
            case LLVAR:
                byte[] llvarLen = new byte[2];
                llvarLen[0] = bts[offset];
                llvarLen[1] = bts[offset + 1];
                int firstLen = Integer.valueOf(new String(llvarLen));
                val = new byte[firstLen];
                System.arraycopy(bts, offset + 2, val, 0, firstLen);
                length = 2 + firstLen;
                break;
            case LLLVAR:
                byte[] lllvarLen = new byte[3];
                lllvarLen[0] = bts[offset];
                lllvarLen[1] = bts[offset + 1];
                lllvarLen[2] = bts[offset + 2];
                int first2Len = Integer.valueOf(new String(lllvarLen));
                val = new byte[first2Len];
                System.arraycopy(bts, offset + 3, val, 0, first2Len);
                length = 3 + first2Len;
                break;
            default:
                break;
        }
        field.setByteValue(val);
        logger.info(field.getId() + "域:" + EncodeUtil.hex(val));
        return length;
    }

    // Byte数组的合并，不同byte数组域将被整合为一个大的byte数组
    private byte[] merge(IsoPackage isoPackage) throws IOException {
        ByteArrayOutputStream byteOutPut = new ByteArrayOutputStream(100);
        for (IsoField field : isoPackage) {
            if (field.isChecked()) {
                // Mac
                if (isoPackage.isMacPos(field.getId())) {
                    try {
                        byteOutPut.write(mac(isoPackage));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                switch (field.getIsoType()) {
                    case LLVAR_NUMERIC:
                        byte[] lengthByte0 = new byte[2];
                        lengthByte0 = msgLength(field.getByteValue().length + "", 2);
                        byteOutPut.write(lengthByte0);
                        break;
                    case LLVAR:
                        byte[] lengthByte = new byte[2];
                        lengthByte = msgLength(field.getByteValue().length + "", 2);
                        byteOutPut.write(lengthByte);
                        break;
                    case LLLVAR:
                        byte[] lengthByte2 = new byte[3];
                        lengthByte2 = msgLength(field.getByteValue().length + "", 3);
                        byteOutPut.write(lengthByte2);
                        break;
                    default:
                        break;
                }
                logger.info(field.getId() + "域:" + EncodeUtil.hex(field.getByteValue()));

                byteOutPut.write(field.getByteValue());
            }
        }
        byte[] beforeSend = byteOutPut.toByteArray();
        byte[] bts = new byte[beforeSend.length + 4];
        byte[] lenArr = msgLength(beforeSend.length);
        //
        System.arraycopy(lenArr, 0, bts, 0, 4);
        System.arraycopy(beforeSend, 0, bts, 4, beforeSend.length);
        return bts;
    }

    // 生成前两个字节的长度位
    // 根据约定不同需要对此方法进行重写
    protected abstract byte[] msgLength(int length);

    /**
     * @param lengthStr 长度字符串
     * @param size      总长度
     * @return
     */
    protected byte[] msgLength(String lengthStr, int size) {
        if (size == 0) {
            return null;
        }
        if (size < lengthStr.length()) {
            throw new IllegalArgumentException("数据 " + lengthStr + "长度超出预设最大长度为:" + size);
        }
        return EncodeUtil.addBlankLeft(lengthStr, size - lengthStr.length(), "0").getBytes();
    }

    // 生成最后一位的MAC加密
    protected abstract byte[] mac(IsoPackage isoPackage) throws Exception;

    // 对返回的数据进行MAC校验
    protected abstract void macValidate(IsoPackage isoPackage, Map<String, String> map);

    public void setMacKey(String macKey) {
        this.macKey = macKey;
    }
}
