package cn.lezoo.doux.common.util.convert.simple8583.util.encrypt;

/**
 * <p>三倍长DES</p>
 */
public class TripleDES extends DES {

    // 双倍长计算密钥长度
    public static String desdouble(String source, String key, int mode) {
        String first = des2(source.substring(0, 16), key, mode);
        String second = des2(source.substring(16), key, mode);
        return first + second;
    }

    public static String getMacValue(String macKey) {
        //java中为什么不支持"0"*32这种写法？
        String data = desdouble("00000000000000000000000000000000", macKey, 0);
        return data.substring(0, 6);
    }

    public static String getMac(String macKey, String data) {
        String lk = macKey.substring(0, 16);
        String rk = macKey.substring(16);
        String secondResult = MacUtil.macAsc(lk, "0000000000000000", data);
        String tripleResult = des1(secondResult, rk, 1);
        return des1(tripleResult, lk, 0);
    }
}
