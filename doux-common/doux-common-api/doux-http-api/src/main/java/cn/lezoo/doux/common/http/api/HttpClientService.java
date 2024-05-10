package cn.lezoo.doux.common.http.api;

import cn.lezoo.doux.common.http.api.dto.HttpRequestDTO;
import cn.lezoo.doux.common.http.api.dto.HttpResponseDTO;

/**
 * @author qipeng
 * @date 2022/9/7 10:09
 * @desc 作为rpc接口暴露服务
 */
public interface HttpClientService {
    /**
     * http请求
     *
     * @param httpRequestDTO 请求内容封装
     * @return 响应请求结果
     */
    HttpResponseDTO request(HttpRequestDTO httpRequestDTO);
}
