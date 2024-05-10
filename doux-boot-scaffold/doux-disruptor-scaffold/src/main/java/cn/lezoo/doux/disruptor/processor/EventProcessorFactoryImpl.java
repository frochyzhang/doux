/*
 * Copyright [2023/2/16] [<a href="mailto:frochyzhang@gmail.com>frochyZhang</a>]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.lezoo.doux.disruptor.processor;

import cn.lezoo.doux.common.util.cache.SyncCache;
import cn.lezoo.doux.disruptor.template.DisruptorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2023/2/16 22:21
 */
@Component
public class EventProcessorFactoryImpl implements EventProcessorFactory {
    private static final Logger logger = LoggerFactory.getLogger(EventProcessorFactoryImpl.class);
    /**
     * 处理器链定义
     */
    private final SyncCache<String, EventProcessor<Object>> processorMap = new SyncCache<>();

    @Override
    public void register(EventProcessor eventProcessor) {
        this.processorMap.put(eventProcessor.processKey(), eventProcessor);
    }

    @Override
    public void process(DisruptorEvent disruptorEvent) {
        processorMap.getOrDefault(disruptorEvent.getEventKey(), new EventProcessor<Object>() {
            @Override
            public String processKey() {
                return "default";
            }

            @Override
            public void process(Object data) {
                logger.error("未匹配处理器: {},丢弃请求数据：{}", disruptorEvent.getEventKey(), data);
            }
        }).process(disruptorEvent.getData());
    }
}
