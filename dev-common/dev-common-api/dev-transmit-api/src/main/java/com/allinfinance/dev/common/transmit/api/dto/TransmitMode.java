package com.allinfinance.dev.common.transmit.api.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qipeng
 * @date 2022/9/9 15:27
 * @desc 文件传输方式：ftp sftp
 */
public enum TransmitMode {
    FTP, SFTP;

    private static final Map<String, TransmitMode> mappings = new HashMap<>(8);

    static {
        for (TransmitMode mode : TransmitMode.values()) {
            mappings.put(mode.name(), mode);
        }
    }

    public static TransmitMode resolve(String name) {
        return name != null ? mappings.get(name) : null;
    }

    public boolean matches(String mode) {
        return (this == resolve(mode));
    }
}
