package com.allinfinance.dev.common.util.common;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RegexHelper
 *
 * @author jiangjf
 * @date 2017/1/11
 */
public class RegexHelper {


    /**
     * 利用正则表达式从文本text中找到夹在两个关键字中间的部分(keyword1)(.+)(keyword2)
     *
     * @param text
     * @param keyword1
     * @param keyword2
     * @return
     */
    public static final String getStrBetweenKeywords(String text, String keyword1, String keyword2) {
        Pattern p = Pattern.compile("(" + keyword1 + ")" +
                        "(.+)" +
                        "(" + keyword2 + ")",
                Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.group(2);
        }
        return null;
    }

    /**
     * 利用正则表达式从文本text中找到夹在两个关键字中间的部分(keyword1)(.+?)(keyword2)，懒惰模式匹配
     *
     * @param text
     * @param keyword1
     * @param keyword2
     * @return
     */
    public static final String[] getLazyStrBetweenKeywords(String text, String keyword1, String keyword2) {
        String[] ret = new String[0];
        Set<String> tmp = new HashSet<String>();
        Pattern p = Pattern.compile(new StringBuffer()
                        .append("(").append(keyword1).append(")")
                        .append("(.+?)")
                        .append("(").append(keyword2).append(")")
                        .toString(),
                Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        while (m.find()) {
            tmp.add(m.group(2));
        }
        return tmp.toArray(ret);
    }


    /**
     * 利用正则表达式检查是否完整匹配
     *
     * @param text
     * @param reg
     * @return
     */
    public static final boolean isMatch(String text, String reg) {
        if (StringUtils.isNotEmpty(text) && StringUtils.isNotBlank(reg)) {
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(text);
            return m.matches();
        }
        return false;
    }

    /**
     * 利用正则表达式检查是否包含
     *
     * @param text
     * @param reg
     * @return
     */
    public static final boolean isContain(String text, String reg) {
        if (StringUtils.isNotEmpty(text) && StringUtils.isNotBlank(reg)) {
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(text);
            return m.find();
        }
        return false;
    }


    private RegexHelper() {
    }

    public static void main(String[] args) {
    }

    /**
     * 校验字符串是否是数字
     *
     * @param message
     * @return
     */
    public static final boolean isDigitals(String message) {
        return RegexHelper.isMatch(message, "^[0-9]+$");
    }
}
