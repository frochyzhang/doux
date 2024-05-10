package cn.lezoo.doux.white.list.diversion.http.service;

public interface WhiteListService {
    boolean isWhiteList(HttpRequestDTO httpRequestDTO) throws Exception;
}
