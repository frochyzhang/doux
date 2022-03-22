package com.allinfinance.dev.batch.scaffold.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author qipeng
 * @date 2022/1/26 10:59
 */

public class JobParamsUtils {
    public static JobParameters defaultJobParams() {
        HashMap<String, JobParameter> paramMap = new HashMap<>();
        JobParameter runTime = new JobParameter(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        paramMap.put("runTime", runTime);
        return new JobParameters(paramMap);
    }

    public static JobParameters parseJobParams(String xxlParameter) {
        HashMap<String, JobParameter> paramMap = new HashMap<>();
        JobParameter runTime = new JobParameter(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        paramMap.put("runTime", runTime);
        String[] split = StringUtils.split(xxlParameter, ";");
        Arrays.stream(split)
                .filter(s -> s.contains(":"))
                .forEach(s -> {
                    String[] param = StringUtils.split(s, ":");
                    paramMap.put(param[0], new JobParameter(param[1]));
                });
        return new JobParameters(paramMap);
    }
}
