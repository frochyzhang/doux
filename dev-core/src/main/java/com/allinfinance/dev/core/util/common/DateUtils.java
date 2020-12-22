package com.allinfinance.dev.core.util.common;

import com.allinfinance.dev.core.constant.CommonConstants;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * DateUtils
 *
 * @author jiangjf
 * @date 2017/1/11
 */
public class DateUtils {

    /**
     * 日期转换
     * @author jared
     * @param time
     * @param fmt : yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatTime(Timestamp time, String fmt) {
        if (time == null) {
            return "";
        }
        SimpleDateFormat myFormat = new SimpleDateFormat(fmt);
        return myFormat.format(time);
    }

    /**
     * 获取系统当前时间（秒）
     * @author jared
     * @return
     */
    public static Timestamp getTime() {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        String mystrdate = myFormat.format(calendar.getTime());
        return Timestamp.valueOf(mystrdate);
    }

    /**
     * 获取当前日期(时间 00:00:00)
     * @author jared
     * @return
     */
    public static Timestamp getDateFirst() {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar calendar = Calendar.getInstance();
        String mystrdate = myFormat.format(calendar.getTime());
        return Timestamp.valueOf(mystrdate);
    }

    /**
     * 获取当前日期(时间 23:59:59)
     * @author jared
     * @return
     */
    public static Timestamp getDateLast() {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Calendar calendar = Calendar.getInstance();
        String mystrdate = myFormat.format(calendar.getTime());
        return Timestamp.valueOf(mystrdate);
    }

    /**
     * 获取当前日期
     * @author jared
     * @return
     */
    public static Date getDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * yyyy-MM-dd HH:mm:ss 转换成 Timestamp
     * @author jared
     * @param timeString
     * @return
     */
    public static Timestamp getTime(String timeString) {
        return Timestamp.valueOf(timeString);
    }
    /**
     * 自定义格式的字符串转换成日期
     * @author jared
     * @param timeString
     * @param fmt
     * @return
     * @throws Exception
     */
    public static Timestamp getTime(String timeString, String fmt) throws Exception {
        SimpleDateFormat myFormat = new SimpleDateFormat(fmt);
        Date date = myFormat.parse(timeString);
        myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return getTime(myFormat.format(date));
    }

    /**
     * 格式化日期
     * @author jared
     * @param date
     * @param fmt
     * @return
     * @throws Exception
     */
    public static String formatDate(Date date, String fmt){
        if (date == null) {
            return "";
        }
        SimpleDateFormat myFormat = new SimpleDateFormat(fmt);
        return myFormat.format(date);
    }

    /**
     * 返回日期或者时间，如果传入的是日期，返回日期的 00:00:00 时间
     * @author jared
     * @param timeString
     * @return
     * @throws Exception
     */
    public static Timestamp getDateFirst(String timeString) throws Exception {
        if (timeString == null || timeString.equals("")) {
            return null;
        }
        if (timeString.length() > 10) {
            return getTime(timeString, "yyyy-MM-dd HH:mm:ss");
        } else {
            return getTime(timeString, "yyyy-MM-dd");
        }
    }

    /**
     * 返回日期或者时间，如果传入的是日期，返回日期的 23:59:59 时间
     * @author jared
     * @param timeString
     * @return
     * @throws Exception
     */
    public static Timestamp getDateLast(String timeString) throws Exception{
        if (timeString == null || timeString.equals("")) {
            return null;
        }
        if (timeString.length() > 10) {
            return getTime(timeString, "yyyy-MM-dd HH:mm:ss");
        } else {
            return getTime(timeString +" 23:59:59", "yyyy-MM-dd HH:mm:ss");
        }
    }

    /**
     * 获取本周周一时间，返回格式 yyyy-MM-dd 00:00:00
     * @author jared
     * @return
     */
    public static Timestamp getMonday(){
        Calendar calendar = Calendar.getInstance();
        int dayofweek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayofweek == 0)
            dayofweek = 7;
        calendar.add(Calendar.DATE, -dayofweek + 1);
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String mystrdate = myFormat.format(calendar.getTime());
        return Timestamp.valueOf(mystrdate);
    }


    /**
     * 获取本周周日时间，返回格式 yyyy-MM-dd 23:59:59
     * @author jared
     * @return
     */
    public static Timestamp getSunday(){
        Calendar calendar = Calendar.getInstance();
        int dayofweek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayofweek == 0)
            dayofweek = 7;
        calendar.add(Calendar.DATE, -dayofweek + 7);
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String mystrdate = myFormat.format(calendar.getTime());
        return Timestamp.valueOf(mystrdate);
    }

    /**
     * 增加天数
     * @author jared
     * @param time
     * @param day
     * @return
     */
    public static Timestamp addDay(Timestamp time, Long day) {
        Timestamp time2 = new Timestamp(time.getTime()+ day * 1000 * 60 * 60 * 24);
        return time2;
    }

    /**
     * 比较 2 个日期格式的字符串
     * @author jared
     * @param str1 格式 ：yyyyMMdd
     * @param str2 格式 ：yyyyMMdd
     * @return
     */
    public static Integer compareDate(String str1, String str2) throws Exception {
        return Integer.parseInt(str1) - Integer.parseInt(str2);
    }

    /**
     * 2 个时间的相差天数
     * @author jared
     * @param time1
     * @param time2
     * @return
     */
    public static Integer getDay(Timestamp time1, Timestamp time2) {
        Long dayTime = (time1.getTime() - time2.getTime()) / (1000 * 60 * 60 * 24);
        return dayTime.intValue();
    }

    /**
     * 获取系统当前时间（分）
     * @author jared
     * @return
     */
    public static String getMinute() {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyyMMddHHmm");
        return myFormat.format(new Date());
    }

    /**
     * 转换成时间 字符串格式必须为 yyyy-MM-dd HH:mm:ss 或 yyyy-MM-dd
     * @author jared
     * @return
     * @throws ParseException
     */
    public static Date parseToDate(String val) throws ParseException{
        Date date = null;
        if (val != null && val.trim().length() != 0 && !val.trim().toLowerCase().equals("null")){
            val = val.trim();
            if (val.length() > 10) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = sdf.parse(val);
            }
            if (val.length() <= 10) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = sdf.parse(val);
            }
        }
        return date;
    }

    /**
     * 获取上月的第一天 yyyy-MM-dd 00:00:00 和最后一天 yyyy-MM-dd 23:59:59
     * @author jared
     * @return
     */
    @SuppressWarnings("static-access")
    public static Map<String, String> getPreMonth() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        calendar.add(Calendar.MONTH, -1);
        Date theDate = calendar.getTime();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first_prevM = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first_prevM).append(" 00:00:00");
        day_first_prevM = str.toString(); //上月第一天

        calendar.add(cal.MONTH, 1);
        calendar.set(cal.DATE, 1);
        calendar.add(cal.DATE, -1);
        String day_end_prevM = df.format(calendar.getTime());
        StringBuffer endStr = new StringBuffer().append(day_end_prevM).append(" 23:59:59");
        day_end_prevM = endStr.toString();  //上月最后一天

        Map<String, String> map = new HashMap<String, String>();
        map.put("prevMonthFD", day_first_prevM);
        map.put("prevMonthPD", day_end_prevM);
        return map;
    }


    /**
     * 获取上周周一时间，返回格式 yyyy-MM-dd 00:00:00
     * @author jared
     * @return
     */
    @SuppressWarnings("static-access")
    public static Timestamp getPreMonday(){
        Calendar calendar = Calendar.getInstance();
        int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println(dayofweek);
        if (dayofweek == 1) {
            calendar.add(calendar.WEEK_OF_MONTH, -1);
        }

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.add(calendar.WEEK_OF_MONTH, -1);

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String mystrdate = myFormat.format(calendar.getTime());
        return Timestamp.valueOf(mystrdate);
    }

    /**
     * 获取上周周日时间，返回格式 yyyy-MM-dd 23:59:59
     * @author jared
     * @return
     */
    @SuppressWarnings("static-access")
    public static Timestamp getPreSunday() {
        Calendar calendar = Calendar.getInstance();
        int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayofweek != 1) {
            calendar.add(calendar.WEEK_OF_MONTH, +1);
        }

        calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        calendar.add(calendar.WEEK_OF_MONTH, -1);

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String mystrdate = myFormat.format(calendar.getTime());
        return Timestamp.valueOf(mystrdate);
    }

    /**
     * 字符串日期加n天
     * @param date 日期
     * @param days 加的天数
     * @param dateFormat 字符串日期格式
     * @return
     */
    public static String addDay(String date, int days, String dateFormat){
        if(dateFormat == null||"".equals(dateFormat)){
            dateFormat = "yyyyMMdd";
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            Calendar cd = Calendar.getInstance();
            cd.setTime(formatter.parse(date));
            cd.add(Calendar.DATE, days);
            return formatter.format(cd.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    // ============================0.获取当前时间====================================

    /**
     * 获取当前日期类型时间
     */
    public static Date getNow() {
        return new Date();
    }

    /**
     * 获取当前时间戳
     */
    public static long getNowTimestamp() {
        return getNow().getTime();
    }

    /**
     * 获取当前日期 yyyyMMdd
     */
    public static String getCurrentDate() {
        return toMailDateDtPartString(getNow());
    }

    /**
     * 获取当期时间HHmmss
     *
     * @return
     */
    public static String getCurrentTime() {
        return toMailTimeTmPartString(getNow());
    }

    /**
     * 获取当期时间MM月dd日HH:mm
     *
     * @return
     */
    public static String getCurrentMmDdHmTime() {
        return toMailDtmPart(getNow());
    }

    /**
     * 获取当前日期和时间yyyyMMddHHmmss
     *
     * @return
     */
    public static String getCurrentDateTime() {
        return toMailDateString(getNow());
    }

    // ============================1.Date2String=====================================

    /**
     * 将一个日期型转换为指定格式字串
     *
     * @param aDate
     * @param formatStr
     * @return
     */
    public static final String toFormatDateString(Date aDate, String formatStr) {
        if (aDate == null) {
            return StringUtils.EMPTY;
        }
        return new SimpleDateFormat(formatStr).format(aDate);

    }

    /**
     * 将一个日期型转换为'yyyy-MM-dd'格式字串
     *
     * @param aDate
     * @return
     */
    public static final String toShortDateString(Date aDate) {
        return toFormatDateString(aDate, SHORT_DATE_FORMAT);
    }

    /**
     * 将一个日期型转换为'yyyyMMdd'格式字串
     *
     * @param aDate
     * @return
     */
    public static final String toMailDateDtPartString(Date aDate) {
        return toFormatDateString(aDate, MAIL_DATE_DT_PART_FORMAT);
    }

    /**
     * 将一个日期型转换为'HHmmss'格式字串
     *
     * @param aDate
     * @return
     */
    public static final String toMailTimeTmPartString(Date aDate) {
        return toFormatDateString(aDate, MAIL_TIME_TM_PART_FORMAT);
    }

    /**
     * 将一个日期型转换为'yyyyMMddHHmmss'格式字串
     *
     * @param aDate
     * @return
     */
    public static final String toMailDateString(Date aDate) {
        return toFormatDateString(aDate, MAIL_DATE_FORMAT);
    }

    /**
     *
     */
    /**
     * 将一个日期型转换为MM月dd日HH:mm格式字串
     *
     * @param aDate
     * @return
     */
    public static final String toMailDtmPart(Date aDate) {
        return toFormatDateString(aDate, MAIL_DATA_DTM_PART_FORMAT);
    }

    /**
     * 将一个日期型转换为yyyy年MM月dd日HH:mm格式字串
     *
     * @param aDate
     * @return
     */
    public static final String toMailYDtmPart(Date aDate) {
        return toFormatDateString(aDate, MAIL_DATA_YDTM_PART_FORMAT);
    }

    /**
     *
     */
    /**
     * 将一个日期型转换为yyyy.MM.dd格式字串
     *
     * @param aDate
     * @return
     */
    public static final String toPointDtmPart(Date aDate) {
        return toFormatDateString(aDate, POINT_DATA_DTM_PART_FORMAT);
    }

    /**
     * 将一个日期型转换为'yyyy-MM-dd HH:mm:ss'格式字串
     *
     * @param aDate
     * @return
     */
    public static final String toLongDateString(Date aDate) {
        return toFormatDateString(aDate, LONG_DATE_FORMAT);
    }

    /**
     * 将一个日期型转换为'HH:mm:ss'格式字串
     *
     * @param aDate
     * @return
     */
    public static final String toLongDateTmPartString(Date aDate) {
        return toFormatDateString(aDate, LONG_DATE_TM_PART_FORMAT);
    }

    /**
     * 将一个日期型转换为'yyyy年MM月dd日'格式字串
     *
     * @param aDate
     * @return
     */
    public static final String toShortDateGBKString(Date aDate) {
        return toFormatDateString(aDate, SHORT_DATE_GBK_FORMAT);
    }

    /**
     * 将一个日期型转换为'yyyy年MM月dd日 HH时mm分'格式字串
     *
     * @param aDate
     * @return
     */
    public static final String toDateGBKString(Date aDate) {
        return toFormatDateString(aDate, DATE_GBK_FORMAT);
    }

    /**
     * 将一个日期型转换为'yyyy年MM月dd日 HH时mm分ss秒'格式字串
     *
     * @param aDate
     * @return
     */
    public static final String toLongDateGBKString(Date aDate) {
        return toFormatDateString(aDate, LONG_DATE_GBK_FORMAT);
    }

    /**
     * 将一个日期型转换为'HH时mm分ss秒'格式字串
     *
     * @param aDate
     * @return
     */
    public static final String toLongDateTmPartGBKString(Date aDate) {
        return toFormatDateString(aDate, Long_DATE_TM_PART_GBK_FORMAT);
    }

    /**
     * 将一个日期型转换为'yyyy-MM-dd HH:mm:ss:SSS'格式字串
     *
     * @param aDate
     * @return
     */
    public static final String toFullDateString(Date aDate) {
        return toFormatDateString(aDate, FULL_DATE_FORMAT);
    }

    /**
     * 将一个日期型转换为'yyyy年MM月dd日 HH时mm分ss秒SSS毫秒'格式字串
     *
     * @param aDate
     * @return
     */
    public static final String toFullDateGBKString(Date aDate) {
        return toFormatDateString(aDate, FULL_DATE_GBK_FORMAT);
    }

    /**
     * 将一个日期型转换为'yyyyMMddHHmmssSSS'格式字串
     *
     * @param aDate
     * @return
     */
    public static final String toFullDateCompactString(Date aDate) {
        return toFormatDateString(aDate, FULL_DATE_COMPACT_FORMAT);
    }

    /**
     * 将一个日期型转换为LDAP格式字串
     *
     * @param aDate
     * @return
     */
    public static final String toLDAPDateString(Date aDate) {
        return toFormatDateString(aDate, LDAP_DATE_FORMAT);
    }

    // ============================2.String2Date=====================================

    /**
     * 将一个符合指定格式的字串解析成日期型
     *
     * @param aDateStr
     * @param formatter
     * @return
     * @throws ParseException
     */
    public static final Date parser(String aDateStr, String formatter) throws ParseException {
        if (StringUtils.isBlank(aDateStr)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatter);
        return sdf.parse(aDateStr);

    }

    /**
     * 将一个符合'yyyy-MM-dd HH:mm:ss'格式的字串解析成日期型
     *
     * @param aDateStr
     * @return
     */
    public static final Date parseLongDateString(String aDateStr) throws ParseException {
        return parser(aDateStr, LONG_DATE_FORMAT);

    }

    /**
     * 将一个符合'yyyy-MM-dd HH:mm:ss'格式的字串解析成日期型
     *
     * @param aDateStr
     * @return
     */
    public static final Date parseLongDateDtPartString(String aDateStr) throws ParseException {
        return parser(aDateStr, LONG_DATE_FORMAT);

    }

    /**
     * 将一个符合'yyyy-MM-dd HH:mm:ss'格式的字串解析成日期型
     *
     * @param aDateStr
     * @return
     */
    public static final Date parseLongDateTmPartString(String aDateStr) throws ParseException {
        return parser(aDateStr, LONG_DATE_FORMAT);

    }

    /**
     * 将一个符合'yyyy-MM-dd'格式的字串解析成日期型
     *
     * @param aDateStr
     * @return
     */
    public static final Date parseShortDateString(String aDateStr) throws ParseException {
        return parser(aDateStr, SHORT_DATE_FORMAT);

    }

    /**
     * 将一个符合'yyyyMMddHHmmss'格式的字串解析成日期型
     *
     * @param aDateStr
     * @return
     */
    public static final Date parseMailDateString(String aDateStr) throws ParseException {
        return parser(aDateStr, MAIL_DATE_FORMAT);

    }

    /**
     * 将一个符合'yyyyMMdd'格式的字串解析成日期型
     *
     * @param aDateStr
     * @return
     */
    public static final Date parseMailDateDtPartString(String aDateStr) throws ParseException {
        return parser(aDateStr, MAIL_DATE_DT_PART_FORMAT);
    }

    /**
     * 将一个符合'HHmmss'格式的字串解析成日期型
     *
     * @param aDateStr
     * @return
     */
    public static final Date parseMailDateTmPartString(String aDateStr) throws ParseException {
        return parser(aDateStr, MAIL_TIME_TM_PART_FORMAT);
    }

    /**
     * 将一个符合'yyyy-MM-dd HH:mm:ss:SSS'格式的字串解析成日期型
     *
     * @param aDateStr
     * @return
     */
    public static final Date parseFullDateString(String aDateStr) throws ParseException {
        return parser(aDateStr, FULL_DATE_FORMAT);

    }

    /**
     * 将一个符合'yyyy-MM-dd'、'yyyy-MM-dd HH:mm:ss'或'EEE MMM dd HH:mm:ss zzz yyyy'格式的字串解析成日期型，
     * 如果为blank则返回空，如果不为blank又不符合格式则报错
     *
     * @param aDateStr
     * @return
     */
    public static Date parseDateString(String aDateStr) {
        Date ret = null;
        if (StringUtils.isNotBlank(aDateStr)) {
            try {
                if (DataValidator.isShortDateStr(aDateStr)) {
                    ret = DateUtils.parseShortDateString(aDateStr);
                } else if (DataValidator.isLongDateStr(aDateStr)) {
                    ret = DateUtils.parseLongDateString(aDateStr);
                } else if (DataValidator.isDateStrMatched(aDateStr, DateUtils.DEFAULT_DATE_FORMAT)) {
                    ret = DateUtils.parser(aDateStr, DateUtils.DEFAULT_DATE_FORMAT);
                } else {
                    throw new IllegalArgumentException("date format mismatch");
                }
            } catch (ParseException e) {
                // 不可能到这里
            }
        }
        return ret;
    }


    /**
     * 转换日期格式 yyyy-MM-dd => yyyyMMdd
     *
     * @param dt yyyy-MM-dd
     * @return yyyyMMdd
     */
    public static String transfer2ShortDate(String dt) {
        if (dt == null || dt.length() != 10) {
            return dt;
        }
        String[] tmp = StringUtils.split(dt, CommonConstants.DASH);
        return tmp[0].concat(StringUtils.leftPad(tmp[1], 2, "0")).concat(
                StringUtils.leftPad(tmp[2], 2, "0"));
    }

    /**
     * 转换日期格式 HH:mm[:ss] => HHmmss
     *
     * @param tm HH:mm[:ss]
     * @return HHmmss
     */
    public static String transfer2ShortTime(String tm) {
        String shorTime = null;
        if (tm == null) {
            return tm;
        }
        String[] tmp = StringUtils.split(tm, CommonConstants.COLON);
        if (tmp.length < 2) {
            return tm;
        }
        if (tmp.length == 3) {
            shorTime = tmp[0].concat(StringUtils.leftPad(tmp[1], 2, "0")).concat(
                    StringUtils.leftPad(tmp[2], 2, "0"));
        } else {
            shorTime = StringUtils.leftPad(tmp[0], 2, "0").concat(
                    StringUtils.leftPad(tmp[1], 2, "0").concat("00"));
        }
        return shorTime;
    }

    /**
     * 转换日期格式 yyyyMMdd => yyyy-MM-dd
     *
     * @param dt yyyyMMdd
     * @return yyyy-MM-dd
     */
    public static String transfer2LongDateDtPart(String dt) {
        if (dt == null || dt.length() != 8) {
            return dt;
        }
        return dt.substring(0, 4).concat(CommonConstants.DASH).concat(dt.substring(4, 6))
                .concat(CommonConstants.DASH).concat(dt.substring(6));
    }

    /**
     * 转换日期格式 HHmmss => HH:mm:ss
     *
     * @param tm HHmmss
     * @return HH:mm:ss
     */
    public static String transfer2LongDateTmPart(String tm) {
        if (tm == null || tm.length() != 6) {
            return tm;
        }
        return tm.substring(0, 2).concat(CommonConstants.COLON).concat(tm.substring(2, 4))
                .concat(CommonConstants.COLON).concat(tm.substring(4));
    }

    /**
     * 转换日期格式 yyyyMMdd => yyyy年MM月dd日
     *
     * @param dt yyyyMMdd
     * @return yyyy年MM月dd日
     */
    public static String transfer2LongDateGbkDtPart(String dt) {
        if (dt == null || dt.length() != 8) {
            return dt;
        }
        return dt.substring(0, 4).concat("年").concat(dt.substring(4, 6)).concat("月")
                .concat(dt.substring(6)).concat("日");
    }

    /**
     * 转换日期格式HHmmss => HH时mm分ss秒
     *
     * @param tm HHmmss
     * @return HH时mm分ss秒
     */
    public static String transfer2LongDateGbkTmPart(String tm) {
        if (tm == null || tm.length() != 6) {
            return tm;
        }
        return tm.substring(0, 2).concat("时").concat(tm.substring(2, 4)).concat("分")
                .concat(tm.substring(4)).concat("秒");
    }

    // ============================4.时间加减=====================================

    /**
     * 为一个日期加上指定年数
     *
     * @param aDate
     * @param amount 年数
     * @return
     */
    public static final Date addYears(Date aDate, int amount) {
        return addTime(aDate, Calendar.YEAR, amount);
    }

    /**
     * 为一个日期加上指定月数
     *
     * @param aDate
     * @param amount 月数
     * @return
     */
    public static final Date addMonths(Date aDate, int amount) {
        return addTime(aDate, Calendar.MONTH, amount);
    }

    /**
     * 为一个日期加上指定天数
     *
     * @param aDate
     * @param amount 天数
     * @return
     */
    public static final Date addDays(Date aDate, int amount) {
        return addTime(aDate, Calendar.DAY_OF_YEAR, amount);
    }

    /**
     * 为一个日期加上指定天数
     *
     * @param aDate yyyyMMdd格式字串
     * @param amount 天数
     * @return
     */
    public static final String addDays(String aDate, int amount) {
        try {
            return DateUtils.toMailDateDtPartString(addTime(
                    DateUtils.parseMailDateDtPartString(aDate), Calendar.DAY_OF_YEAR,
                    amount));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 为一个日期加上指定小时数
     *
     * @param aDate
     * @param amount 小时数
     * @return
     */
    public static final Date addHours(Date aDate, int amount) {
        return addTime(aDate, Calendar.HOUR, amount);

    }

    /**
     * 为一个日期加上指定分钟数
     *
     * @param aDate
     * @param amount 分钟数
     * @return
     */
    public static final Date addMinutes(Date aDate, int amount) {
        return addTime(aDate, Calendar.MINUTE, amount);
    }

    /**
     * 为一个日期加上指定秒数
     *
     * @param aDate
     * @param amount 秒数
     * @return
     */
    public static final Date addSeconds(Date aDate, int amount) {
        return addTime(aDate, Calendar.SECOND, amount);

    }

    private static final Date addTime(Date aDate, int timeType, int amount) {
        if (aDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        cal.add(timeType, amount);
        return cal.getTime();
    }

    // ======================================5.时间国际化=================================

    /**
     * 得到当前时间的UTC时间
     *
     * @return
     */
    public static final String getUTCTime() {
        return getSpecifiedZoneTime(Calendar.getInstance().getTime(), TimeZone.getTimeZone("GMT+0"));
    }

    /**
     * 得到指定时间的UTC时间
     *
     * @param aDate 时间戳
     * @return yyyy-MM-dd HH:mm:ss 格式
     */
    public static final String getUTCTime(Date aDate) {
        return getSpecifiedZoneTime(aDate, TimeZone.getTimeZone("GMT+0"));
    }

    /**
     * 得到当前时间的指定时区的时间
     *
     * @param tz
     * @return
     */
    public static final String getSpecifiedZoneTime(TimeZone tz) {
        return getSpecifiedZoneTime(Calendar.getInstance().getTime(), tz);

    }

    /**
     * 得到指定时间的指定时区的时间
     *
     * @param aDate 时间戳,Date是一个瞬间的long型距离历年的位移偏量， 在不同的指定的Locale/TimeZone的jvm中，它toString成不同的显示值，
     *            所以没有必要为它再指定一个TimeZone变量表示获取它时的jvm的TimeZone
     *
     * @param tz 要转换成timezone
     *
     * @return yyyy-MM-dd HH:mm:ss 格式
     */
    public static final String getSpecifiedZoneTime(Date aDate, TimeZone tz) {
        if (aDate == null) {
            return StringUtils.EMPTY;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATE_FORMAT);
        sdf.setTimeZone(tz);
        return sdf.format(aDate);
    }

    // ==================================6. miscellaneous==========================

    /**
     * 计算两个日期之间相差的月数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static final int getDifferenceMonths(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        return Math.abs((startCal.get(Calendar.YEAR) - endCal.get(Calendar.YEAR)) * 12
                + (startCal.get(Calendar.MONTH) - endCal.get(Calendar.MONTH)));
    }

    /**
     * 计算两个日期之间相差的月数
     *
     * @param startDateStr yyyy-mm-dd
     * @param endDateStr yyyy-mm-dd
     * @return
     */
    public static final int getDifferenceMonths(String startDateStr, String endDateStr) {
        if (!DataValidator.isShortDateStr(startDateStr)) {
            throw new RuntimeException("startDateStr does't match yyyy-MM-dd pattern");
        }
        if (!DataValidator.isShortDateStr(endDateStr)) {
            throw new RuntimeException("endDateStr does't match yyyy-MM-dd pattern");
        }
        try {
            return getDifferenceMonths(parseShortDateString(startDateStr),
                    parseShortDateString(endDateStr));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param startDateStr yyyy-mm-dd
     * @param endDateStr yyyy-mm-dd
     * @return
     */
    public static final int getDifferenceDays(String startDateStr, String endDateStr) {
        return new Long(getDifferenceMillis(startDateStr, endDateStr) / (NANO_ONE_DAY)).intValue();
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param startDateStr yyyymmdd
     * @param endDateStr yyyymmdd
     * @return
     */
    public static final int getDifferenceDays2(String startDateStr, String endDateStr) {
        return new Long(getDifferenceMillis(startDateStr, endDateStr, MAIL_DATE_DT_PART_FORMAT)
                / (NANO_ONE_DAY)).intValue();
    }

    /* ------- start ------------ */
    /**
     * 两个日期之间相减（存在负数）
     *
     * @param startDateStr yyyy-mm-dd
     * @param endDateStr yyyy-mm-dd
     * @return
     */
    public static final int getDaysSubtract(String startDateStr, String endDateStr) {
        return new Long(getDaysSubtractMillis(startDateStr, endDateStr) / (NANO_ONE_DAY))
                .intValue();
    }

    /**
     * 两个日期之间相减（存在负数）判断日期有效性
     *
     * @param startDateStr yyyy-mm-dd
     * @param endDateStr yyyy-mm-dd
     * @return
     * @throws ParseException
     */
    public static final int getDaysSub(String startDateStr, String endDateStr)
            throws ParseException {
        DateFormat df = new SimpleDateFormat(SHORT_DATE_FORMAT);
        df.setLenient(false);

        df.parse(startDateStr);
        df.parse(endDateStr);

        return getDaysSubtract(startDateStr, endDateStr);
    }

    /**
     * 两个日期之间相减（存在负数）
     *
     * @param startDateStr yyyymmdd
     * @param endDateStr yyyymmdd
     * @return
     */
    public static final int getDaysSubtract2(String startDateStr, String endDateStr) {
        return new Long(getDaysSubtractMillis(startDateStr, endDateStr, MAIL_DATE_DT_PART_FORMAT)
                / (NANO_ONE_DAY)).intValue();
    }

    /**
     * 两个日期之间相减（存在负数）
     *
     * @param startDateStr yyyy-mm-dd
     * @param endDateStr yyyy-mm-dd
     * @return
     * @throws ParseException
     */
    public static final long getDaysSubtractMillis(String startDateStr, String endDateStr) {
        return getDaysSubtractMillis(startDateStr, endDateStr, SHORT_DATE_FORMAT);
    }

    /**
     * 两个日期之间相减（存在负数）
     *
     * @param startDateStr yyyymmddhhmmss
     * @param endDateStr yyyymmddhhmmss
     * @return
     * @throws ParseException
     */
    public static final long getDaysSubtractMillis2(String startDateStr, String endDateStr) {
        return getDaysSubtractMillis(startDateStr, endDateStr, MAIL_DATE_FORMAT);
    }

    /**
     * 相隔天数
     *
     * @param startDateStr yyyymmdd
     * @param endDateStr yyyymmdd
     * @return
     * @throws ParseException
     */
    public static final boolean isBetweenTheDay(String startDateStr, String endDateStr, int field,
                                                int n) {
        try {
            Date date1 = parser(startDateStr, MAIL_DATE_DT_PART_FORMAT);
            Date date2 = parser(endDateStr, MAIL_DATE_DT_PART_FORMAT);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            if (cal1.after(cal2)) {
                cal2.add(field, n);
                return !cal1.after(cal2);
            } else {
                cal1.add(field, n);
                return !cal2.after(cal1);
            }
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 相隔天数,如相隔一年的时间，指20150120-20160119
     *
     * @param startDateStr yyyymmdd
     * @param endDateStr yyyymmdd
     * @return
     * @throws ParseException
     */
    public static final boolean isBetweenTheDayExt(String startDateStr, String endDateStr,
                                                   int field, int n) {
        try {
            Date date1 = parser(startDateStr, MAIL_DATE_DT_PART_FORMAT);
            Date date2 = parser(endDateStr, MAIL_DATE_DT_PART_FORMAT);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            if (cal1.after(cal2)) {
                cal2.add(field, n);
                return !(cal1.after(cal2) || cal1.equals(cal2));
            } else {
                cal1.add(field, n);
                return !(cal2.after(cal1) || cal2.equals(cal1));
            }
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 计算两个日期之间相差的的毫秒数（存在负数）
     *
     * @param startDateStr
     * @param endDateStr
     * @param dateFormat
     * @return
     */
    public static final long getDaysSubtractMillis(String startDateStr, String endDateStr,
                                                   String dateFormat) {
        try {
            return getDaysSubtractMillis(parser(startDateStr, dateFormat),
                    parser(endDateStr, dateFormat));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 计算两个日期之间相差的的毫秒数（存在负数）
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static final long getDaysSubtractMillis(Date startDate, Date endDate) {
        return endDate.getTime() - startDate.getTime();
    }

    /* ------- end ------------ */

    /**
     * 计算两个日期之间相差的天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static final int getDifferenceDays(Date startDate, Date endDate) {
        return new Long(getDifferenceMillis(startDate, endDate) / (NANO_ONE_DAY)).intValue();

    }

    /**
     * 计算两个日期之间相差的的毫秒数
     *
     * @param startDateStr yyyy-mm-dd
     * @param endDateStr yyyy-mm-dd
     * @return
     * @throws ParseException
     */
    public static final long getDifferenceMillis(String startDateStr, String endDateStr) {
        return getDifferenceMillis(startDateStr, endDateStr, SHORT_DATE_FORMAT);
    }

    /**
     * 计算两个日期之间相差的的毫秒数
     *
     * @param startDateStr yyyyMMddHHmmss
     * @param endDateStr yyyyMMddHHmmss
     * @return
     * @throws ParseException
     */
    public static final long getDifferenceMillis2(String startDateStr, String endDateStr) {
        return getDifferenceMillis(startDateStr, endDateStr, MAIL_DATE_FORMAT);
    }

    /**
     * 计算两个日期之间相差的的毫秒数
     *
     * @param startDateStr
     * @param endDateStr
     * @param dateFormat
     * @return
     */
    public static final long getDifferenceMillis(String startDateStr, String endDateStr,
                                                 String dateFormat) {
        try {
            return getDifferenceMillis(parser(startDateStr, dateFormat),
                    parser(endDateStr, dateFormat));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 计算两个日期之间相差的的毫秒数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static final long getDifferenceMillis(Date startDate, Date endDate) {
        return Math.abs(endDate.getTime() - startDate.getTime());
    }

    /**
     * 检验 日期是否在指定区间内，如果格式错误，返回false
     *
     * 如果maxDateStr或minDateStr为空则比较时变为正负无穷大，如果都为空，则返回false
     *
     * @param aDate
     * @param minDateStr 必须是yyyy-MM-dd格式，时分秒为00:00:00
     * @param maxDateStr 必须是yyyy-MM-dd格式，时分秒为00:00:00
     * @return
     */
    public static final boolean isDateBetween(Date aDate, String minDateStr, String maxDateStr) {
        boolean ret = false;
        try {
            Date dMaxDate = null;
            Date dMinDate = null;
            dMaxDate = DateUtils.parseShortDateString(maxDateStr);
            dMinDate = DateUtils.parseShortDateString(minDateStr);
            switch ((dMaxDate != null ? 5 : 3) + (dMinDate != null ? 1 : 0)) {
                case 6:
                    ret = aDate.before(dMaxDate) && aDate.after(dMinDate);
                    break;
                case 5:
                    ret = aDate.before(dMaxDate);
                    break;
                case 4:
                    ret = aDate.after(dMinDate);
            }
        } catch (ParseException e) {
        }
        return ret;
    }

    /**
     * 计算某日期所在月份的天数
     *
     * @param aDateStr yyyy-mm-dd
     * @return
     */
    public static final int getDaysInMonth(String aDateStr) {
        if (DataValidator.isShortDateStr(aDateStr)) {
            throw new RuntimeException("aDateStr des't match yyyy-MM-dd pattern");
        }
        try {
            return getDaysInMonth(parseShortDateString(aDateStr));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 计算某日期所在月份的天数
     *
     * @param aDate
     * @return
     */
    public static final int getDaysInMonth(Date aDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当天是星期几
     *
     * @param dt
     * @return
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 是否是yyyyMMdd格式
     *
     * @param dateStr
     * @return
     */
    public static final boolean isShortDateStr(String dateStr) {
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
    public static final boolean isTimestampStr(String aDateStr) {
        try {
            DateUtils.parseMailDateString(aDateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * 获取出生年月日YYMMDD
     *
     * @param certId
     * @return
     */
    public static final String getBirthdayByCertId(String certId) {

        if (!DataValidator.is18CertId(certId)) {
            return null;
        }
        return certId.replaceAll("^\\d{6}(\\d{8}).+", "$1");
    }


    public static final String convertFmt(String dateStr,String oldFmt, String newFmt) throws ParseException{
        if(StringUtils.isEmpty(dateStr)||StringUtils.isEmpty(oldFmt)||StringUtils.isEmpty(newFmt)) {
            return null;
        }
        Date date = parser(dateStr, oldFmt);
        return toFormatDateString(date,newFmt);
    }

    public static Date parseDateAndTime(String value){
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            date = sdf.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static final String SHORT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String SHORT_DATE_GBK_FORMAT = "yyyy年MM月dd日";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String DATE_GBK_FORMAT = "yyyy年MM月dd日 HH时mm分";
    public static final String LONG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String LONG_DATE_GBK_FORMAT = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String MAIL_DATE_FORMAT = "yyyyMMddHHmmss";
    public static final String MAIL_DATE_HHMM_FORMAT = "HH:mm";
    public static final String FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String FULL_DATE_GBK_FORMAT = "yyyy年MM月dd日 HH时mm分ss秒SSS毫秒";
    public static final String FULL_DATE_COMPACT_FORMAT = "yyyyMMddHHmmssSSS";
    public static final String LDAP_DATE_FORMAT = "yyyyMMddHHmm'Z'";
    public static final String US_LOCALE_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";

    public static final String MAIL_DATE_DT_PART_FORMAT = "yyyyMMdd";
    public static final String MAIL_TIME_TM_PART_FORMAT = "HHmmss";
    public static final String LONG_DATE_TM_PART_FORMAT = "HH:mm:ss";
    public static final String Long_DATE_TM_PART_GBK_FORMAT = "HH时mm分ss秒";
    public static final String MAIL_DATA_DTM_PART_FORMAT = "MM月dd日HH:mm";
    public static final String MAIL_DATA_YDTM_PART_FORMAT = "yyyy年MM月dd日  HH:mm";
    public static final String POINT_DATA_DTM_PART_FORMAT = "yyyy.MM.dd";
    public static final String FULL_DATE_LIGHT_FORMAT = "yyyyMMdd HH:mm:ss";
    public static final String CUPS_FULL_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String DEFAULT_DATE_FORMAT = US_LOCALE_DATE_FORMAT;

    public static final long NANO_ONE_SECOND = 1000;
    public static final long NANO_ONE_MINUTE = 60 * NANO_ONE_SECOND;
    public static final long NANO_ONE_HOUR = 60 * NANO_ONE_MINUTE;
    public static final long NANO_ONE_DAY = 24 * NANO_ONE_HOUR;
}
