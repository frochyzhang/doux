package cn.lezoo.doux.recoup.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface MetaRegistryApi {
    @PostMapping("/meta/registry")
    void registry(@RequestBody MetaRegistryCmd cmd);
}
