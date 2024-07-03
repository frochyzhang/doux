package cn.lezoo.doux.datasource.scaffold.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/3/24
 **/
@Data
@Accessors(chain = true)
@Configuration
@ConfigurationProperties(prefix = "doux.datasource.transaction")
public class TransactionPointProperties {
    public static final String TRANSACTION_ENABLE = "doux.datasource.transaction.enabled";

    /**
     * 是否开启事务
     */
    private boolean enabled = true;
    /**
     * point-cut表达式
     */
    private String expression = "execution(* cn.lezoo.*..dal.service.Tbl*.*(..)) || execution(* cn.lezoo.*..infrastructure.service.Tbl*.*(..))";
}
