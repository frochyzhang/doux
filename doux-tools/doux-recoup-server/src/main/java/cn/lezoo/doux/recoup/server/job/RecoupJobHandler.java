package cn.lezoo.doux.recoup.server.job;

import cn.hutool.json.JSONUtil;
import cn.lezoo.doux.dispatch.scaffold.api.IJobHandler;
import cn.lezoo.doux.recoup.api.EventProcessApi;
import cn.lezoo.doux.recoup.api.EventProcessCmd;
import cn.lezoo.doux.recoup.server.AppClusterRepository;
import cn.lezoo.doux.recoup.server.AppInfo;
import cn.lezoo.doux.recoup.server.RecoupEvent;
import cn.lezoo.doux.recoup.server.RecoupEventRepository;
import feign.Feign;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2024/1/29 21:13
 */
@Slf4j
@Component
public class RecoupJobHandler implements IJobHandler {
    private final RecoupEventRepository recoupEventRepository;
    private final AppClusterRepository appClusterRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public RecoupJobHandler(AppClusterRepository appClusterRepository,
                            RecoupEventRepository recoupEventRepository) {
        this.appClusterRepository = appClusterRepository;
        this.recoupEventRepository = recoupEventRepository;
    }

    @Override
    public String dispatcherName() {
        return "recoupJob";
    }

    @Override
    public void execute() throws Exception {
        // 轮询扫描
        // todo: 分页
        Map<String, List<RecoupEvent>> eventGroups = entityManager.createQuery(
                        "select e from RecoupEvent e where e.status is false and e.nextPollingTime <= :now and e.currentRecoupTimes < e.maxRecoupTimes",
                        RecoupEvent.class)
                .setParameter("now", LocalDateTime.now())
                .getResultList()
                .stream().collect(Collectors.groupingBy(RecoupEvent::getAppName));

        Map<String, List<AppInfo>> appInfoMap =
                appClusterRepository.findByStatusTrue().collect(Collectors.groupingBy(AppInfo::getAppName));


        // todo：我不想要一次只传一条记录给应用，最好能传递一个list给应用，但是这里有个要注意的地方就是events需要先根据应用group
        // todo：应用处理完成后需要返回处理状态回服务端，服务端需要根据处理状态跟新事件状态。
        eventGroups.forEach((key, value) -> {
            AppInfo selected = serverBalance(appInfoMap.get(key));

            List<EventProcessCmd> list =
                    value.stream().map(event -> EventProcessCmd.builder()
                            .className(event.getClassName())
                            .data(event.getEventData())
                            .build()
                    ).collect(Collectors.toList());

            // 回传
            Map<Long, Boolean> processResult = Feign.builder()
                    .encoder((object, bodyType, template) -> {
                        String bodyText = JSONUtil.toJsonPrettyStr(object);
                        log.info("请求内容：{}", bodyText);
                        template.body(bodyText);
                    })
                    .decoder(new Decoder.Default())
                    .target(EventProcessApi.class, "http://" + selected.getHost() + ":" + selected.getPort())
                    .process(list);


            // 更新事件状态
            processResult.forEach((k, v) -> {
                Query jpql;
                if (v) {
                    jpql = entityManager.createQuery(
                            "update RecoupEvent set status = true where id = :id"
                    );
                } else {
                    jpql = entityManager.createQuery(
                            "update RecoupEvent set currentRecoupTimes = currentRecoupTimes + 1 ,nextPollingTime = :now where id = :id"
                    ).setParameter("now", LocalDateTime.now());
                }
                jpql.setParameter("id", k).executeUpdate();
            });
        });
    }

    public synchronized AppInfo serverBalance(List<AppInfo> appInfos) {
        int totalWeight = 0;
        AppInfo selectedNode = null;

        for (AppInfo e : appInfos) {
            totalWeight += e.getWeight();
            e.setCurrentWeight(e.getWeight());
            if (selectedNode == null) {
                selectedNode = e;
            } else if (e.getCurrentWeight() > selectedNode.getCurrentWeight()) {
                selectedNode = e;
            }
        }

        assert selectedNode != null;
        selectedNode.setCurrentWeight(selectedNode.getCurrentWeight() - totalWeight);

        return appClusterRepository.save(selectedNode);
    }
}
