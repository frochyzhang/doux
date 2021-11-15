package com.allinfinance.dev.core.constant;

import java.util.Locale;

/*
 * @author 张勇
 * @date 2020-11-28 01:25
 */
public class CommonConstants {

    public static final String STRING_CLASS_NAME = String.class.getName();
    public static final String FILE_PREFIX = "socket-";
    public static final String FILE_SUF_FIX = ".properties";
    public static final String FILE_PARENT_PATH = "socket-config-path";
    public static final String DEFAULT_MAPPER_PACKAGE = "com.allinfinance.dev.batch.dao.mapper";
    public static final String XML_BEAN_VALIDATOR_REQUIRE = "dev.xml.field.verify";
    public static final String KEY_SESSION_CLIENT_IP = "KEY_SESSION_CLIENT_IP";
    // -------------------------------【SYMBOL】-----------------------//
    public static final String BLANK = " ";
    public static final String EMPTY = "";
    public static final String SPACE = " ";
    public static final String BANG = "!";
    public static final String QUESTION_MARK = "?";
    public static final String COMMA = ",";
    public static final String POINT = ".";
    public static final String COLON = ":";
    public static final String SEMICOLON = ";";
    public static final String COLON_ZH = "：";
    public static final String SEMICOLON_ZH = "；";
    public static final String QUOTE = "'";
    public static final String SINGLE_QUOTE = "\'";
    public static final String DOUBLE_QUOTE = "\"";
    public static final String STAR = "*";
    public static final String PLUS = "+";
    public static final String DASH = "-";
    public static final String EQUAL = "=";
    public static final String SLASH = "/";
    public static final String BACK_SLASH = "\\";
    public static final String PIPE = "|";
    public static final String UNDERLINE = "_";
    public static final String DOLOR = "$";
    public static final String AT = "@";
    public static final String CROSS_HATCH = "#";
    public static final String PERCENT = "%";
    public static final String AND = "&";
    public static final String CIRCUMFLEX = "^";
    public static final String TILDE = "~";
    public static final String LEFT_BRACE = "{";
    public static final String RIGHT_BRACE = "}";
    public static final String LEFT_BRACKET = "[";
    public static final String RIGHT_BRACKET = "]";
    public static final String LEFT_ANGLE_BRACKET = "<";
    public static final String RIGHT_ANGLE_BRACKET = ">";
    public static final String LEFT_PARENTHESES = "(";
    public static final String RIGHT_PARENTHESES = ")";

    public static final char CHAR_BLANK = ' ';

    public static final String LINE_BR_SYMBOL_LINUX = "\r\n";
    public static final String LINE_CHANGE_SYMBOL = "\n";
    public static final String ENTER_SYMBOL = "\r";
    public static final String HORIZEONTAL_TAB = "\t";
    // -----------------------------【***FIX】-----------------------//
    public static final String CLASSPATH_PREFIX = "classpath:";
    public static final String HBMFILES_SUFFIX = ".hbm.xml";
    public static final String PROPERTIES_SUFFIX = ".properties";
    public static final String CLASS_SUFFIX = ".class";
    // -----------------------------【LOCALE】-----------------------//
    public static final String[] ALL_LOCALES_STRING = {Locale.ENGLISH.toString(),
            Locale.FRENCH.toString(), Locale.GERMAN.toString(), Locale.ITALIAN.toString(),
            Locale.JAPANESE.toString(), Locale.KOREAN.toString(), Locale.CHINESE.toString(),
            Locale.SIMPLIFIED_CHINESE.toString(), Locale.TRADITIONAL_CHINESE.toString(),
            Locale.FRANCE.toString(), Locale.GERMANY.toString(), Locale.ITALY.toString(),
            Locale.JAPAN.toString(), Locale.KOREA.toString(), Locale.CHINA.toString(),
            Locale.PRC.toString(), Locale.TAIWAN.toString(), Locale.UK.toString(),
            Locale.US.toString(), Locale.CANADA.toString(), Locale.CANADA_FRENCH.toString()

    };
    public static final String DEFAUL_CHARSET = "UTF-8";
    public static final String CHINESE_CHARSET = "GBK";
    public static final String ISO_CHARSET = "ISO-8859-1";
    public static final String CHARGE_LIST = "charge_list";
    public static final String INTERFACE_LIST = "interface_list";
    public static final String ROUTE_LIST = "route_list";
    public static final String IFS_SELECT = "/select";
    public static final String IFS_DELETE = "/delete";
    public static final String IFS_UPLOAD = "/upload";
    public static final String IFS_DOWNLOAD = "/download";
    public static final String IFS_SYSTEM_ID = "systemId";
    public static final String IFS_FILE_ID = "fileId";
    public static final String IFS_SYSTEM_PWD = "password";
    // 文件类型
    public static final String IFS_IMAGE_JPEG = "image/jpeg";
    public static final String IFS_IMAGE_JPG = "image/jpg";
    public static final String IFS_IMAGE_PNG = "image/png";
    public static final String IFS_IMAGE_BMP = "image/bmp";
    public static final String IFS_IMAGE_GIF = "image/gif";
}
