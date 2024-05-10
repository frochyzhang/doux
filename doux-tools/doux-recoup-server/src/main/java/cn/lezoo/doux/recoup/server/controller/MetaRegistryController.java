package cn.lezoo.doux.recoup.server.controller;

import cn.lezoo.doux.recoup.api.MetaRegistryApi;
import cn.lezoo.doux.recoup.api.MetaRegistryCmd;
import cn.lezoo.doux.recoup.server.AppClusterRepository;
import cn.lezoo.doux.recoup.server.AppInfo;
import cn.lezoo.doux.recoup.server.RecoupEvent;
import cn.lezoo.doux.recoup.server.RecoupEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2024/1/23 16:17
 */
@Slf4j
@RestController
@RequestMapping
public class MetaRegistryController implements MetaRegistryApi {

    private final RecoupEventRepository recoupEventRepository;
    private final AppClusterRepository appClusterRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public MetaRegistryController(AppClusterRepository appClusterRepository,
                                  RecoupEventRepository recoupEventRepository) {
        this.appClusterRepository = appClusterRepository;
        this.recoupEventRepository = recoupEventRepository;
    }

    @Override
    public void registry(MetaRegistryCmd cmd) {
        log.info("请求内容：{}", cmd);
        //  保存应用信息
        AppInfo appInfo = appClusterRepository.saveAndFlush(
                AppInfo.builder()
                        .appName(cmd.getAppName())
                        .host(cmd.getHost())
                        .port(cmd.getPort())
                        .build()
        );
        // 保存事件信息
        RecoupEvent recoupEvent = recoupEventRepository.saveAndFlush(
                RecoupEvent.builder()
                        .className(cmd.getMetaData().getClassName())
                        .eventData(cmd.getMetaData().getData())
                        .appName(cmd.getAppName())
                        .build()
        );

        log.info("处理完成");
    }

}
