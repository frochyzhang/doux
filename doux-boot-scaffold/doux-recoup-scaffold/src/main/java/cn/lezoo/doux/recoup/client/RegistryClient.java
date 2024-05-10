package cn.lezoo.doux.recoup.client;

import cn.lezoo.doux.recoup.api.MetaRegistryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2024/1/23 15:30
 */
@FeignClient(name = "recoup-client", url = "http://localhost:8080", path = "/")
public interface RegistryClient extends MetaRegistryApi {
}
