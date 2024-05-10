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

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2023/2/16 22:18
 */
public abstract class AbstractEventProcessor<T> implements EventProcessor<T> {

    @Autowired
    private EventProcessorFactory factory;

    @PostConstruct
    public void init() {
        factory.register(this);
    }
}
