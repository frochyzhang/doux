package cn.lezoo.doux.datasource.scaffold.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/3/24
 **/
@Configuration
@ConfigurationProperties(prefix = "cn.lezoo.datasource.transaction")
public class TransactionPointProperties {
    public static final String TRANSACTION_ENABLE = "cn.lezoo.datasource.transaction.enabled";

    /**
     * 是否开启事务
     */
    private boolean enabled = true;
    /**
     * point-cut表达式
     */
    private String expression = "execution(* cn.lezoo.*..dal.service.Tbl*.*(..)) || execution(* cn.lezoo.*..infrastructure.service.Tbl*.*(..))";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
