package cn.lezoo.doux.datasource.scaffold.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author qipeng
 * @date 2022/1/21 10:48
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "doux.datasource.encrypt")
@Configuration
public class DatasourceEncryptProperties {
    /**
     * 是否加密
     */
    private boolean encrypted;
    /**
     * 加密公钥
     */
    private String publicKey;
}
