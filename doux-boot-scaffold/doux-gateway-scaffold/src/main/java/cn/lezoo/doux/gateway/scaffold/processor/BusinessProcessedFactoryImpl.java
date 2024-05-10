package cn.lezoo.doux.gateway.scaffold.processor;

import cn.lezoo.doux.gateway.scaffold.processor.api.BusinessProcessedFactory;
import cn.lezoo.doux.gateway.scaffold.processor.api.BusinessProcessor;
import cn.lezoo.doux.gateway.scaffold.processor.dto.AbstractRequestDTO;
import cn.lezoo.doux.gateway.scaffold.processor.dto.AbstractResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/16 16:52
 */
@Component
public class BusinessProcessedFactoryImpl implements BusinessProcessedFactory {

    private static final Map<String, BusinessProcessor> PROCESSORS = new ConcurrentHashMap<>();

    /**
     * 注册业务处理器
     *
     * @param processor 业务处理器
     */
    @Override
    public void register(BusinessProcessor processor) {
        PROCESSORS.put(processor.processorKey(), processor);
    }

    /**
     * 业务处理
     *
     * @param abstractRequestDTO 请求字符串
     * @param processorKey       处理器类型
     * @return 脱敏后字符串
     */
    @Override
    public AbstractResponseDTO processed(AbstractRequestDTO abstractRequestDTO, String processorKey) {
        if (processorKey == null) {
            throw new NullPointerException("ProcessorKey不能为空");
        }
        String tmp = processorKey.contains("?") ? processorKey.split("\\?")[0] : processorKey;
        BusinessProcessor businessProcessor = PROCESSORS.get(tmp);
        if (businessProcessor == null) {
            throw new IllegalArgumentException(processorKey + " 未匹配对应实现!");
        }
        return businessProcessor.process(abstractRequestDTO);
    }
}
