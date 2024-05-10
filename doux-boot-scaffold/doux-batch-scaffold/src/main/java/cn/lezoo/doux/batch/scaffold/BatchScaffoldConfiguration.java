package cn.lezoo.doux.batch.scaffold;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author qipeng
 * @date 2022/1/21 16:42
 */
@ComponentScan(basePackages = BatchScaffoldConfiguration.SCAFFOLD_BASE_PACKAGE_PREFIX)
@Configuration
@MapperScan(basePackages = {"cn.lezoo.doux.batch.dao.mapper"})
public class BatchScaffoldConfiguration {
    public static final String SCAFFOLD_BASE_PACKAGE_PREFIX = "cn.lezoo.doux.batch.scaffold";
    private static final Logger logger = LoggerFactory.getLogger(BatchScaffoldConfiguration.class);

    public BatchScaffoldConfiguration() {
        logger.info("Batch脚手架加载成功!");
    }

}
