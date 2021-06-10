package com.allinfinance.dev.core.util.convert.simple8583.key;


/**
 * <p>存储一些关键字.</p>
 */
public interface SimpleConstants {


    //	public static String MSG_LENGTH = "MsgLength";
    public static String MTI = "mti";
    public static String TPDU = "tpdu";
    public static String VERSION_NO = "VersionNo";
    public static String BIT_MAP = "BitMap";
    //通联规范字符编码为ASCII
//    public static String ENCODING = "ASCII";
    public static String ENCODING = "GBK";

    //TODO 暂时写死mac密钥，因为不用做mac处理，后续可完善
    public static String MAC_KEY = "helloSimple8583";

}
