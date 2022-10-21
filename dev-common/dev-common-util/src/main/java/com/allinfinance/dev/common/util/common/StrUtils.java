package com.allinfinance.dev.common.util.common;


import com.allinfinance.dev.common.util.constant.CommonConstants;

import javax.validation.constraints.NotNull;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

/**
 * StrUtils
 *
 * @author jiangjf
 * @date 2017/1/12
 */
public class StrUtils {
    /**
     * 已分隔符来输出传入的字符串
     *
     * @param split
     * @param obj
     * @return
     * @author jared
     */
    public static String toStringSpilt(String split, String... obj) {
        if (obj == null) {
            return "";
        }

        StringBuffer sbBuffer = new StringBuffer();
        for (String s : obj) {
            if (s != null && !"".equals(s)) {
                if (sbBuffer.length() == 0) {
                    sbBuffer.append(s);
                } else {
                    sbBuffer.append(split).append(s);
                }
            }
        }

        return sbBuffer.toString();
    }


    /**
     * 按照长度获取字符串，如果超出截取最大长度，后面加...
     *
     * @param str
     * @param len
     * @return
     * @author jared
     */
    public static String maxString(String str, Integer len) {
        if (str == null) {
            return str;
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(0, len) + "...";
    }

    /**
     * 判断传入参数,如果是Null或者空值，返回false，不为空返回true
     *
     * @param checkAll True:所有的为空才返回,False:只要有一个为空返回
     * @param objects
     * @return
     * @author jared
     */
    public static Boolean checkNull(Boolean checkAll, Object... objects) {
        Boolean ret = true;
        if (objects == null) {
            return false;
        }
        for (Object s : objects) {
            if (s == null || "".equals(s.toString().trim())) {
                if (!checkAll) {
                    return false;
                } else {
                    ret = false;
                }
            }
        }
        return ret;
    }


    /**
     * 返回等长字符，如果前缀+字符串>长度，返回字符串
     *
     * @param prefix 前缀
     * @param len
     * @param str
     * @return
     * @author jared
     */
    public static String getMaxLength(String prefix, int len, String str) {
        if (!checkNull(false, str) || str.length() >= len) {
            return str;
        }

        if (prefix.length() + str.length() > len) {
            return str;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        for (int i = 0; i < len - prefix.length() - str.length(); i++) {
            sb.append("0");
        }
        sb.append(str);

        return sb.toString();
    }

    /**
     * 字符串分隔
     *
     * @param split
     * @param obj
     * @return
     * @author jared
     */
    public static String[] toStringSpilt(String split, String obj) {
        if (obj == null) {
            return new String[0];
        }

        String[] strSplit = obj.split(split);
        return strSplit;
    }

    /**
     * 验证List是否为空
     *
     * @param list
     * @return
     * @author jared
     */
    @SuppressWarnings("rawtypes")
    public static Boolean checkList(List list) {
        if (list != null && list.size() != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 过滤特殊字符,只要是判断网页的参数传递
     * 目前过滤的是 <,>,&
     *
     * @param str
     * @return
     * @throws Exception
     * @author jared
     */
    public static String filterSpecial(@NotNull(message = "filterSpecial方法参数不能为null") String str) {
        String res = str.replaceAll("<[^>]*>", "");
        res = res.replaceAll("<", "");
        res = res.replaceAll(">", "");
        res = res.replaceAll("&", "");

        return res;
    }


    /**
     * 判断是否包含特殊字符串
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static boolean hasFilterSpecial(String str) {
        try {
            if (str != null && filterSpecial(str).equals(str)) {
                return true;
            }
        } catch (NullPointerException e) {
            throw new NullPointerException("filterSpecial方法抛出空指针,");
        }
        return false;

    }


    /**
     * 获得随机密码方法
     */
    public static String getRandomNum(int pwd_len) throws NoSuchAlgorithmException {

        Random r = SecureRandom.getInstanceStrong();
        // 35是因为数组是从0开始的，26个字母+10个数字
        final int maxNum = 36;
        int i; // 生成的随机数
        int count = 0; // 生成的密码的长度
        char[] str = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        StringBuffer pwd = new StringBuffer("");
        while (count < pwd_len) {
            // 生成随机数，取绝对值，防止生成负数，

            i = Math.abs(r.nextInt(maxNum));

            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }
        return pwd.toString();
    }

    public static String leftAddChar(String seq, int length, String s) {

        if (seq.length() > length) {
            return seq;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length - seq.length(); i++) {
            sb.append(s);
        }
        sb.append(seq);
        return sb.toString();
    }


    public static String rightAddChar(String seq, int length, String s) {

        if (seq.length() > length) {
            return seq;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(seq);
        for (int i = 0; i < length - seq.length(); i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * strs中是否包含str
     *
     * @param str
     * @param strs
     * @return
     */
    public static boolean isContainStr(String str, String... strs) {
        if (strs != null && org.apache.commons.lang3.StringUtils.isNotBlank(str)) {
            for (int i = 0; i < strs.length; i++) {
                if (str.equals(strs[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 拼接字符串
     *
     * @param strs
     * @return
     */
    public static String concatStrs(String... strs) {
        StringBuilder sb = new StringBuilder();
        for (String str : strs) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(str)) {
                sb.append(str.trim());
            }
        }
        return sb.toString();
    }


    /**
     * 左填充字符串
     *
     * @param str
     * @param size
     * @param padStr
     * @return
     */
    public static String leftPad(String str, int size, String padStr) {
        return org.apache.commons.lang3.StringUtils.leftPad(str, size, padStr);

    }

    /**
     * 生成公共流水
     *
     * @param seqId
     * @return
     */
    public static String getCommonSeq(String seqId) {
        return DateUtils.getCurrentDate() + leftPad(seqId, 10, "0");
    }

    /**
     * kchen@20170904:日志脱敏感，字符串填充函数
     *
     * @param string
     * @param filler
     * @param totalLength
     * @param atEnd
     * @return
     */
    public static String fillString(String string, char filler,
                                    int totalLength, boolean atEnd) {
        byte[] tempbyte = (string == null ? "" : string).getBytes();
        int currentLength = tempbyte.length;
        int delta = totalLength - currentLength;
        for (int i = 0; i < delta; i++) {
            if (atEnd) {
                string += filler;
            } else {
                string = filler + string;
            }
        }
        return string;
    }

    /**
     * kchen@20170904:日志脱敏感，将卡号转为前六后四方式
     *
     * @param cardNo
     * @return
     */
    public static String encryptCardNo(String cardNo) {
        String encryptCardNo = null;
        if (cardNo != null && cardNo.length() > 10) {
            int encryptLen = cardNo.length() - 10;
            encryptCardNo = cardNo.substring(0, encryptLen) + fillString(CommonConstants.EMPTY, '*', encryptLen, true) + cardNo.substring(cardNo.length() - 4);
            return encryptCardNo;
        } else {
            return cardNo;
        }


    }

    /**
     * kchen@20170904:日志脱敏感，将手机号显示前三后四
     *
     * @param phoneNo
     * @return
     */
    public static String encryptPhoneNo(String phoneNo) {
        String encryptPhoneNo = null;
        if (phoneNo != null && phoneNo.length() > 7) {
            int encryptLen = phoneNo.length() - 7;
            encryptPhoneNo = phoneNo.substring(0, 3) + fillString(CommonConstants.EMPTY, '*', encryptLen, true) + phoneNo.substring(phoneNo.length() - 4);
            return encryptPhoneNo;
        } else {
            return phoneNo;
        }
    }

    /**
     * kchen@2017021:日志脱敏感，将证件号显示后4位
     *
     * @param IdNo
     * @return
     */
    public static String encryptIdNo(String IdNo) {
        String encryptPhoneNo = null;
        if (IdNo != null && IdNo.length() > 4) {
            int encryptLen = IdNo.length() - 4;
            encryptPhoneNo = fillString(CommonConstants.EMPTY, '*', encryptLen, true) + IdNo.substring(IdNo.length() - 4);
            return encryptPhoneNo;
        } else {
            return IdNo;
        }
    }

    /**
     * xml报文中敏感信息过滤
     *
     * @param data
     * @return
     */
    public static String hide(String data) {
        return hideSense1(hideSense(data));
    }

    private static String hideSense(String data) {
        return data.replaceAll("<IDNo>\\w{14}", "<IDNo>**************").replaceAll("<RcverNm>\\w*</RcverNm>", "<RcverNm>******</RcverNm>").replaceAll("<MobNo>\\d*</MobNo>", "<MobNo>***********</MobNo>");
    }

    private static String hideSense1(String data) {
        return data.replaceAll("AcctId>\\d{12}", "AcctId>************").replaceAll("<PyeeNm>\\w+</PyeeNm>", "<PyeeNm>******</PyeeNm>").replaceAll("<PyerNm>\\w*</PyerNm>", "<PyerNm>******</PyerNm>");
    }
}
