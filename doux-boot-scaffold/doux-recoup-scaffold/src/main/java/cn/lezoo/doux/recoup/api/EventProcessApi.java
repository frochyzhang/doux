package cn.lezoo.doux.recoup.api;

import feign.Headers;
import feign.RequestLine;

import java.util.List;
import java.util.Map;

public interface EventProcessApi {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @RequestLine("POST /event/process")
    Map<Long, Boolean> process(List<EventProcessCmd> cmd);
}
