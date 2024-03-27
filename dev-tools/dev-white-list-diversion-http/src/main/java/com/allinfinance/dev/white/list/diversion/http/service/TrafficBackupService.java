package com.allinfinance.dev.white.list.diversion.http.service;

/**
 * @author huanghf
 * @date 2024/3/26 19:49
 */
public interface TrafficBackupService {
    String getTradeId(HttpRequestDTO httpRequestDTO);

    String getSource(HttpRequestDTO httpRequestDTO);

    String getDestination(HttpRequestDTO httpRequestDTO);
}
