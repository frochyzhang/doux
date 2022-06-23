package com.allinfinance.dev.hsp.util;

/**
 * @author huanghf
 * @date 2022/6/21 11:21
 */
public class StrUtil {
    public static String getLengthStr(String str, int length) {
        if (str.length() >= length) {
            return str.substring(str.length() - length);
        } else {
            StringBuilder newStr = new StringBuilder(str);
            for (int i = 0; i < length - str.length(); i++) {
                newStr.insert(0, "0");
            }
            return newStr.toString();
        }
    }
}
