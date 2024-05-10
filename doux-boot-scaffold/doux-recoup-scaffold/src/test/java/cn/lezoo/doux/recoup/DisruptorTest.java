package cn.lezoo.doux.recoup;

import cn.lezoo.doux.disruptor.template.DisruptorTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

/**
 * @author qipeng
 * @date 2022/6/17 16:17
 * @description
 */
@SpringBootTest
public class DisruptorTest {

    private static DisruptorTemplate disruptorTemplate;

    @Autowired
    public void setDisruptorTemplate(DisruptorTemplate disruptorTemplate) {
        DisruptorTest.disruptorTemplate = disruptorTemplate;
    }

    @Test
    public void test() throws Exception {
        IntStream.rangeClosed(1, 1000 * 1000 * 10).parallel().forEach(i -> {
            disruptorTemplate.publishEvent("test", "请求第" + i + "次");
        });
    }
}
