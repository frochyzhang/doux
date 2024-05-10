package cn.lezoo.doux.recoup.endpoint;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.json.JSONUtil;
import cn.lezoo.doux.disruptor.template.DisruptorTemplate;
import cn.lezoo.doux.recoup.api.EventProcessCmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2024/1/23 16:50
 */
@Slf4j
@RestController
@RequestMapping
public class RecoupEventEndpoint {
    private final DisruptorTemplate disruptorTemplate;

    public RecoupEventEndpoint(DisruptorTemplate disruptorTemplate) {
        this.disruptorTemplate = disruptorTemplate;
    }

    @PostMapping("/event/process")
    public void process(@RequestBody EventProcessCmd cmd) {
        log.info("请求内容：{}", cmd);
        Object bean = JSONUtil.toBean(cmd.getData(), ClassUtil.loadClass(cmd.getClassName()));
        log.info("反序列化后的内容:{}", bean);
        log.info("处理完成");
    }

    @GetMapping("/test")
    public String test() {
        // 1
        // 2
        // 3
        // 4
        disruptorTemplate.publishEvent("test", EventProcessCmd.builder()
                .data("test")
                .className("test")
                .build());
        return "success";
    }
}
