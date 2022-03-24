package com.allinfinance.dev.datasource.scaffold.config;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.*;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/3/19 00:11
 */
@Aspect
@ConditionalOnProperty(value = "com.allinfinance.datasource.transaction.enabled", havingValue = "true")
@Configuration
public class GlobalTransactionConfig {
    private static final int TX_METHOD_TIMEOUT = 60;

    @Autowired
    private TransactionPointProperties transactionPointProperties;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public TransactionInterceptor txAdvice() {
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);
        // 如果当前存在事务，则加入该事务，否则创建一个新的事务
        readOnlyTx.setPropagationBehavior(RuleBasedTransactionAttribute.PROPAGATION_REQUIRED);

        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
        // 异常回滚
        requiredTx.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        requiredTx.setPropagationBehavior(RuleBasedTransactionAttribute.PROPAGATION_REQUIRED);
        requiredTx.setTimeout(TX_METHOD_TIMEOUT);

        RuleBasedTransactionAttribute noTx = new RuleBasedTransactionAttribute();
        noTx.setPropagationBehavior(RuleBasedTransactionAttribute.PROPAGATION_NOT_SUPPORTED);

        Map<String, TransactionAttribute> txMap = new ConcurrentHashMap<>(16);
        //写事务（C、U、D）
        txMap.put("add*", requiredTx);
        txMap.put("save*", requiredTx);
        txMap.put("insert*", requiredTx);
        txMap.put("update*", requiredTx);
        txMap.put("delete*", requiredTx);
        txMap.put("remove*", requiredTx);

        //读事务（R）
        txMap.put("get*", readOnlyTx);
        txMap.put("query*", readOnlyTx);
        txMap.put("find*", readOnlyTx);
        txMap.put("select*", readOnlyTx);
        txMap.put("fetch*", readOnlyTx);
        txMap.put("list*", readOnlyTx);
        txMap.put("count*", readOnlyTx);
        txMap.put("exist*", readOnlyTx);
        txMap.put("search*", readOnlyTx);

        //无事务（T）
        txMap.put("noTX*", noTx);


        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        source.setNameMap(txMap);

        TransactionInterceptor txAdvice = new TransactionInterceptor();
        txAdvice.setTransactionManager(transactionManager);
        txAdvice.setTransactionAttributeSource(source);
        return txAdvice;
    }

    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(transactionPointProperties.getExpression());
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }

}

