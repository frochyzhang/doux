package com.allinfinance.dev.common.api.transmit.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qipeng
 * @date 2022/9/9 15:39
 * @desc 使用方式：从本地上传到远程，从远程下载到本地，从远程1传输到远程2
 */
public enum UsingMethod {
    DOWNLOAD, UPLOAD, BOTH;

    private static final Map<String, UsingMethod> mappings = new HashMap<>(8);

    static {
        for (UsingMethod method : UsingMethod.values()) {
            mappings.put(method.name(), method);
        }
    }

    public static UsingMethod resolve(String name) {
        return name != null ? mappings.get(name) : null;
    }

    public boolean matches(String method) {
        return (this == resolve(method));
    }
}
