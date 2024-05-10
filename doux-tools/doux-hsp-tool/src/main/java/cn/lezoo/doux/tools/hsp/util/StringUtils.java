package cn.lezoo.doux.tools.hsp.util;

import java.util.Random;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2023-04-04 11:45
 */
public class StringUtils {
    public static String getRandomString(int length) {

        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
