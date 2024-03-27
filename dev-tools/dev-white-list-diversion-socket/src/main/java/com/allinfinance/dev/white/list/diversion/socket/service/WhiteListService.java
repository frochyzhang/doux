package com.allinfinance.dev.white.list.diversion.socket.service;

public interface WhiteListService {
    /**
     * 根据传入的请求报文判断是否在白名单中
     *
     * @param reqMsg       请求报文
     * @return 是否在白名单中
     */
    boolean isWhiteList(String reqMsg);
}
