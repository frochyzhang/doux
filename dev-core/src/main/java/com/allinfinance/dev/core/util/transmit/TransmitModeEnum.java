package com.allinfinance.dev.core.util.transmit;

import java.util.Arrays;

/**
 * TransmitModeEnum
 *
 * @author hongmr
 * @date 2017/2/22
 */
public enum TransmitModeEnum {
    MODE_FTP("00", "FTP"),
    MODE_SFTP("01", "SFTP");

    public static TransmitModeEnum valueByCode(String code) {
        return Arrays.stream(TransmitModeEnum.values())
                .filter(enu -> enu.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(code));
    }

    public static TransmitModeEnum valueByDesc(String desc) {
        return Arrays.stream(TransmitModeEnum.values())
                .filter(enu -> enu.description.equals(desc))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(desc));
    }

    private String code;
    private String description;

    TransmitModeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }
}
