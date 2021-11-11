package com.allinfinance.dev.core.util.convert.common;

import java.math.BigDecimal;

/**
 * BizUtils
 *
 * @author hongmr
 * @date 2017/5/18
 */
public class BizUtils {
    private static final BigDecimal HUNDRED = new BigDecimal(100);

    public static String toAmtStr(BigDecimal amt) {
        return amt.multiply(HUNDRED).setScale(0, BigDecimal.ROUND_FLOOR)
                .toString();
    }

    public static String toXAmtStr(BigDecimal amt, String dbcrFlag, int amtLen) {
        return dbcrFlag + ConvertUtils.fillString(amt.multiply(HUNDRED).setScale(0, BigDecimal.ROUND_FLOOR)
                .toString(), '0', amtLen, false);
    }

    public static BigDecimal toBigDecimal(String amt) {
        return new BigDecimal(amt).divide(HUNDRED).setScale(2,
                BigDecimal.ROUND_FLOOR);
    }
}
