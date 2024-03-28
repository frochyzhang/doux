package com.allinfinance.dev.white.list.diversion.http.backup;

import com.allinfinance.dev.white.list.diversion.http.util.LogUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;

/**
 * @author huanghf
 * @date 2024/3/26 14:58
 */
public class TrafficBackupUtils {
    private static final Logger LOGGER = LogUtils.logger(TrafficBackupUtils.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());

    public static void writeLog(BackupInfo backupInfo) throws JsonProcessingException {
        LOGGER.info("message - {} #=%,;,%=#", OBJECT_MAPPER.writeValueAsString(backupInfo));
    }
}
