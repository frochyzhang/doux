package cn.lezoo.doux.datasource.scaffold.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author qipeng
 * @date 2022/1/4 13:49
 */
@Configuration
public class DevDatasourceProperties {
    private static final String DATASOURCE_PREFIX = "doux.datasource";

    @Primary
    @ConfigurationProperties(prefix = DATASOURCE_PREFIX)
    @Bean(name = "dataSourceProperties")
    public DataSourceProperties dataSource() {
        return new DataSourceProperties();
    }

}

