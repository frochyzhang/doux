package com.allinfinance.dev.batch.batch;

import com.allinfinance.dev.core.constant.CommonConstants;
import com.xxl.job.core.context.XxlJobHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2021/7/24 20:34
 */
public class BatchParameterHelper {
    private static final String RUN_MONTH_KEY = "run.month";

    public static HashMap<String, String> getXxlParameters() {
        HashMap<String, String> map = new HashMap<>(16);
        map.put(RUN_MONTH_KEY, String.valueOf(System.currentTimeMillis()));
        Arrays.stream(XxlJobHelper.getJobParam().split(CommonConstants.LINE_CHANGE_SYMBOL))
                .filter(StringUtils::isNotBlank)
                .forEach(keyValue -> {
                    String[] split = keyValue.split(CommonConstants.EQUAL);
                    map.put(split[0], split[1]);
                });
        return map;
    }
}
