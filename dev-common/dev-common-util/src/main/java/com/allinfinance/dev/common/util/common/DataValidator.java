package com.allinfinance.dev.common.util.common;

import cn.hutool.core.lang.Assert;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * DataValidator
 *
 * @author jiangjf
 * @date 2017/1/11
 */
public class DataValidator {

    /**
     * 检验 日期是否在指定区间内，如果格式错误，返回false
     * <p>
     * 如果maxDateStr或minDateStr为空则比较时变为正负无穷大，如果都为空，则返回false
     *
     * @param aDate
     * @param minDateStr 必须是yyyy-MM-dd格式，时分秒为00:00:00
     * @param maxDateStr 必须是yyyy-MM-dd格式，时分秒为00:00:00
     * @return
     */
    public static final boolean isDateBetween(Date aDate, String minDateStr, String maxDateStr) {
        return DateUtils.isDateBetween(aDate, minDateStr, maxDateStr);
    }

    /**
     * 检查字符串是否为空，并且长度是否为指定值
     *
     * @param str
     * @param l   长度
     * @return
     */
    public static final boolean isStrLenEqual(String str, int l) {
        return (StringUtils.isNotBlank(str) && (str.trim().length() == l));
    }

    /**
     * 检查字符串是否为空，并且长度是否小于指定值
     *
     * @param str
     * @param l   长度
     * @return
     */
    public static final boolean isStrLenLess(String str, int l) {
        return (StringUtils.isNotBlank(str) && (str.trim().length() < l));
    }

    /**
     * 检查字符串是否为空，并且长度是否小等于指定值
     *
     * @param str
     * @param l   长度
     * @return
     */
    public static final boolean isStrLenLessEqual(String str, int l) {
        return (StringUtils.isNotBlank(str) && (str.trim().length() <= l));
    }

    /**
     * 判断（如"0123","123L","12.3S"等带有小数点和后缀的）字串，是否代表数字类型
     *
     * @param str
     * @return
     */
    public static final boolean isNumber(String str) {
        return NumberUtils.isNumber(str);
    }

    /**
     * 检查（如"0123","123L","12.3S"等带有小数点和后缀的）字串，是否代表数字类型
     *
     * @param str
     */
    public static final void checkNumber(String str) {
        Assert.isTrue(isNumber(str), "'" + str + "' must be a Number format here.");
    }

    /**
     * 判断字符串中只含有数字字符
     *
     * @param str
     * @return
     */
    public static final boolean isDigits(String str) {
        return NumberUtils.isDigits(str);
    }

    /**
     * 判断Long、Integer、Short、Double、Float等数字是否为空或者0
     *
     * @param number
     * @return
     */
    public static final boolean isNumberNotNullOrZero(Number number) {
        return (number != null && number.doubleValue() != 0);
    }

    /**
     * 检查Long、Integer、Short、Double、Float等数字是否为空或者0
     *
     * @param number
     */
    public static final void checkNumberNotNullOrZero(Number number) {
        Assert.isTrue(isNumberNotNullOrZero(number), "Number must not be null or zero.");
    }

    /**
     * 判断字串是否符合yyyy-MM-dd格式
     *
     * @param dateStr
     * @return
     */
    public static final boolean isShortDateStr(String dateStr) {

        if (StringUtils.isBlank(dateStr)) {
            return false;
        }

        String dateEl = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]"
                + "|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1]"
                + "[0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|"
                + "[3579][26])00))-02-29)";

        Pattern pattern = Pattern.compile(dateEl);
        return pattern.matcher(dateStr).matches();
    }

    /**
     * 检查字串是否符合yyyy-MM-dd格式
     *
     * @param aDateStr
     */
    public static final void checkShortDateStr(String aDateStr) {
        Assert.isTrue(isShortDateStr(aDateStr), "The str-'" + aDateStr
                + "' must match 'yyyy-MM-dd' format.");
    }

    /**
     * 判断字串是否符合yyyy-MM-dd HH:mm:ss格式
     *
     * @param aDateStr
     * @return
     */
    public static final boolean isLongDateStr(String aDateStr) {
        try {
            DateUtils.parseLongDateString(aDateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * 检查字串是否符合yyyy-MM-dd HH:mm:ss格式
     *
     * @param aDateStr
     */
    public static final void checkLongDateStr(String aDateStr) {
        Assert.isTrue(isLongDateStr(aDateStr), "The str-'" + aDateStr
                + "' must match 'yyyy-MM-dd HH:mm:ss' format.");
    }

    /**
     * 判断字串是否符合yyyyMMddHHmmss格式
     *
     * @param aDateStr
     * @return
     */
    public static final boolean isMailDateStr(String aDateStr) {
        try {
            DateUtils.parseMailDateString(aDateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * 判断字串是否符合yyyyMMdd格式
     *
     * @param aDateStr
     * @return
     */
    public static final boolean isMailDateDtPartStr(String aDateStr) {
        if (StringUtils.isBlank(aDateStr)) {
            return false;
        }

        String dateEl = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]"
                + "|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1]"
                + "[0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|"
                + "[3579][26])00))0229)";

        Pattern pattern = Pattern.compile(dateEl);
        return pattern.matcher(aDateStr).matches();
    }

    /**
     * 判断字串是否符合yyyyMMdd格式
     *
     * @param dateStr
     * @return
     */
    public static final boolean isShortMailDateStr(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return false;
        }

        String dateEl = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]"
                + "|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1]"
                + "[0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|"
                + "[3579][26])00))0229)";

        Pattern pattern = Pattern.compile(dateEl);
        return pattern.matcher(dateStr).matches();
    }

    /**
     * 检查字串是否符合yyyyMMddHHmmss格式
     *
     * @param aDateStr
     */
    public static final void checkMailDateStr(String aDateStr) {
        Assert.isTrue(isMailDateStr(aDateStr), "The str-'" + aDateStr
                + "' must match 'yyyyMMddHHmmss' format.");
    }

    /**
     * 判断字串是否符合指定的日期格式
     *
     * @param aDateStr
     * @return
     */
    public static final boolean isDateStrMatched(String aDateStr, String formatter) {
        try {
            DateUtils.parser(aDateStr, formatter);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * 判断对象数组是否为空
     *
     * @param object
     * @return
     */
    public static final boolean isArrayNotEmpty(Object[] object) {
        return !ArrayUtils.isEmpty(object);
    }

    /**
     * 检查对象数组是否为空
     *
     * @param object
     */
    public static final void checkArrayNotEmpty(Object[] object) {
        Assert.notEmpty(object);
    }

    /**
     * 判断身份证号是否正确
     *
     * @param certId
     * @return
     */
    public static final boolean isCertId(String certId) {
        return RegexHelper.isMatch(certId, "\\d{15}|\\d{17}[\\dXx]");
    }

    /**
     * 判断身份证号是否正确-18位
     *
     * @param certId
     * @return
     */
    public static final boolean is18CertId(String certId) {
        return RegexHelper.isMatch(certId, "\\d{17}[\\dXx]");
    }

    /**
     * 判断ip格式是否正确
     *
     * @param ip
     * @return
     */
    public static final boolean isIPAddress(String ip) {
        return RegexHelper.isMatch(ip, "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
    }

    /**
     * 判断密码是否6到12位 false 为非6-12位或汉字
     */
    public static final boolean isPasswordFormat(String password) {
        return RegexHelper.isMatch(password, "^(.{6,12})$")
                && RegexHelper.isMatch(password, "^([\\x00-\\xff]+)");
    }

    /**
     * 判断email格式是否正确
     *
     * @param email
     * @return
     */
    public static final boolean isEmail(String email) {
        return RegexHelper.isMatch(email,
                "^([a-zA-Z0-9_\\.\\-]+)(@{1})([a-zA-Z0-9_\\.\\-]+)(\\.[a-zA-Z0-9]{1,3})$");
    }

    /**
     * 判断电话号码格式是否正确
     *
     * @param phoneNum
     * @return
     */
    public static final boolean isPhoneNum(String phoneNum) {
        return RegexHelper
                .isMatch(phoneNum,
                        "^((\\(\\d{2,3}\\))|(\\d{3}\\-))?(\\(0\\d{2,3}\\)|0\\d{2,3}-)?[1-9]\\d{6,7}(\\-\\d{1,4})?$");
    }

    /**
     * 判断手机号码格式是否正确
     *
     * @param cellPhoneNum
     * @return
     */
    public static final boolean isCellPhoneNum(String cellPhoneNum) {
        /*
         * 2011-05-09 lele.feng 手机号格式判断1开头11位数字 start
         */
        // return
        // RegexHelper.isMatch(cellPhoneNum,"^((\\(\\d{2,3}\\))|(\\d{3}\\-))?1(3|5|8)\\d{9}$");
        return RegexHelper.isMatch(cellPhoneNum, "^((\\(\\d{2,3}\\))|(\\d{3}\\-))?1\\d{10}$");
        /*
         * 2011-05-09 lele.feng end
         */
    }

    /**
     * 判断邮编格式是否正确
     *
     * @param postalcode
     * @return
     */
    public static final boolean isPostalcode(String postalcode) {
        return RegexHelper.isMatch(postalcode, "^[1-9]\\d{5}$");
    }

    /**
     * 判断是否正确银行卡号8-30位
     */
    public static final boolean isCardNo(String cardNo) {
        return RegexHelper.isMatch(cardNo, "(^[0-9]{8,30}$)");
    }

    /**
     * 校验字符串是否是字母和数字的组合
     *
     * @param message
     * @return
     */
    public static final boolean isNumAndLetter(String message) {
        return RegexHelper.isMatch(message, "^[A-Za-z0-9]+$");
    }

    /**
     * 校验字符串是否是字母或数字的组合
     *
     * @param message
     * @return
     */
    public static final boolean isNumOrLetter(String message) {
        return RegexHelper.isMatch(message, "^([A-Za-z]||[0-9])+$");
    }

    /**
     * 校验字符串是否是字母
     *
     * @param message
     * @return
     */
    public static final boolean isLetter(String message) {
        return RegexHelper.isMatch(message, "^[A-Za-z]+$");
    }

    /**
     * 校验基金代码是否6位数字组合
     *
     * @param fundCode
     * @return
     */
    public static final boolean isFundCode(String fundCode) {
        return RegexHelper.isMatch(fundCode, "[0-9]{6}");
    }

    /**
     * 校验省份地区信息是否是4位数字
     *
     * @param areaId
     * @return
     */
    public static final boolean isAreaId(String areaId) {
        return RegexHelper.isMatch(areaId, "[0-9]{4}");
    }

    /**
     * 校验银行代码是否是8位数字
     *
     * @param bankCode
     * @return
     */
    public static final boolean isBankCode(String bankCode) {
        return RegexHelper.isMatch(bankCode, "[0-9]{8}");
    }

    /**
     * 校验省份是否是4位数字
     *
     * @param provinceId
     * @return
     */
    public static final boolean isProvinceId(String provinceId) {
        return RegexHelper.isMatch(provinceId, "[0-9]{4}");
    }

    /**
     * 判断是否是客户号
     *
     * @param custId
     * @return
     */
    public static final boolean isCustId(String custId) {
        if (StringUtils.isBlank(custId)) {
            return false;
        }
        if (custId.getBytes().length != 16) {
            return false;
        }
        if (!isDigits(custId)) {
            return false;
        }
        return true;
    }

    /**
     * 校验是否是指定精度的数字
     *
     * @param doubleAmt
     * @param p         最大长度
     * @param s         小数点后位数
     * @return
     */
    public static boolean isDoubleAmtParam(String doubleAmt, int p, int s) {
        if (p - s < 1) {
            return false;
        }
        String reg = "^[1-9][0-9]{0," + (p - s - 2) + "}+\\.[0-9]{" + s + "}$";
        String regs = "0{1}+\\.[0-9]{" + s + "}$";
        return RegexHelper.isMatch(doubleAmt, reg) || RegexHelper.isMatch(doubleAmt, regs) ? true
                : false;

    }

    /**
     * 校验是否是指定精度的数字且大于0
     *
     * @param doubleAmt
     * @param p         最大长度
     * @param s         小数点后位数
     * @return
     */
    public static boolean isDoubleAmt(String doubleAmt, int p, int s) {
        if (p - s < 1) {
            return false;
        }
        String reg = "^[1-9][0-9]{0," + (p - s - 2) + "}+\\.[0-9]{" + s + "}$";
        String regs = "0{1}+\\.[0-9]{" + s + "}$";
        if (RegexHelper.isMatch(doubleAmt, reg) || RegexHelper.isMatch(doubleAmt, regs)) {
            if (Double.valueOf(doubleAmt) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验是否是指定精度的数字可以为0
     *
     * @param doubleAmt
     * @param p         最大长度
     * @param s         小数点后位数
     * @return
     */
    public static boolean isDoubleAmtOrZero(String doubleAmt, int p, int s) {
        if (p - s < 1) {
            return false;
        }
        String reg = "^[1-9][0-9]{0," + (p - s - 2) + "}+\\.[0-9]{" + s + "}$";
        String regs = "0{1}+\\.[0-9]{" + s + "}$";
        if (RegexHelper.isMatch(doubleAmt, reg) || RegexHelper.isMatch(doubleAmt, regs)) {
            if (Double.valueOf(doubleAmt) >= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证字符长度，中文两个字节
     *
     * @param str
     * @param maxLen
     * @return
     */
    public static boolean lessEqualsMaxLen(String str, int maxLen) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        int len = str.length();
        int bytes = len;
        for (int i = 0; i < len; i++) {
            if (str.charAt(i) < 0 || str.charAt(i) > 255) {
                bytes++;
            }
        }
        return bytes <= maxLen;
    }

    /**
     * 验证字符长度，中文两个字节
     *
     * @param str
     * @param maxLen
     * @return
     */
    public static boolean equalsMaxLen(String str, int maxLen) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        int len = str.length();
        int bytes = len;
        for (int i = 0; i < len; i++) {
            if (str.charAt(i) < 0 || str.charAt(i) > 255) {
                bytes++;
            }
        }
        return bytes == maxLen;
    }

    /**
     * 验证是否是正确日期格式
     *
     * @param str
     * @return
     */
    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyyMMdd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }

    /**
     * 校验购买比例格式，以0开头，且大于0
     *
     * @param doubleRatio
     * @param p           最大长度
     * @param s           小数点后位数
     * @return
     */
    public static boolean isDoubleRatio(String doubleRatio, int p, int s) {
        if (p - s < 1) {
            return false;
        }
        String regs = "^0{1}+\\.[0-9]{" + s + "}$";
        if (RegexHelper.isMatch(doubleRatio, regs)) {
            if (Double.valueOf(doubleRatio) > 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isUrl(String url) {
        String regEx = "^http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?$";
        return RegexHelper.isMatch(url, regEx);
    }

    public static boolean isIp(String ip) {
        String regEx = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        return RegexHelper.isMatch(ip, regEx);
    }
}
