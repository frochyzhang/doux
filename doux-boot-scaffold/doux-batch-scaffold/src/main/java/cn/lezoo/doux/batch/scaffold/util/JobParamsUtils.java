package cn.lezoo.doux.batch.scaffold.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qipeng
 * @date 2022/1/26 10:59
 */

public class JobParamsUtils {
    /**
     * 生成默认的JobParameters，仅携带批量时间一个参数
     *
     * @return JobParameters
     */
    public static JobParameters defaultJobParams() {
        HashMap<String, JobParameter> paramMap = new HashMap<>();
        JobParameter runTime = new JobParameter(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        paramMap.put("runTime", runTime);
        return new JobParameters(paramMap);
    }

    /**
     * 根据xxl-job传过来的参数解析成JobParameters，
     * xxl-job参数格式举例：fileName:testFile;userName:testUser
     *
     * @param xxlParameter 字符串形式的job参数
     * @return JobParameters
     */
    public static JobParameters parseJobParams(String xxlParameter) {
        return new JobParameters(parseJobParamsToMap(xxlParameter));
    }

    /**
     * 根据xxl-job传过来的参数解析成HashMap，
     * xxl-job参数格式举例：fileName:testFile;userName:testUser
     *
     * @param xxlParameter 字符串形式的job参数
     * @return Map<String, JobParameter>
     */
    public static Map<String, JobParameter> parseJobParamsToMap(String xxlParameter) {
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

        return paramMap;
    }
}
