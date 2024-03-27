package com.allinfinance.dev.white.list.diversion.http.service;

public interface WhiteListService {
    boolean isWhiteList(HttpRequestDTO httpRequestDTO) throws Exception;
}
