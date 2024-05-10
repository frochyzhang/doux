package cn.lezoo.doux.framework.http.driver;

import cn.lezoo.doux.framework.extension.annotation.Extensible;
import cn.lezoo.doux.framework.http.driver.dto.HttpRequest;
import cn.lezoo.doux.framework.http.driver.dto.HttpResponse;

/**
 * @author qipeng
 * @date 2022/9/7 14:24
 * @desc 底层http客户端需要实现的接口
 */
@Extensible
public interface SimpleHttp {
    /**
     * 初始化客户端基本的配置
     */
    void init();

    /**
     * 发送请求
     *
     * @param httpRequest
     * @return
     */
    HttpResponse execute(HttpRequest httpRequest);
}
