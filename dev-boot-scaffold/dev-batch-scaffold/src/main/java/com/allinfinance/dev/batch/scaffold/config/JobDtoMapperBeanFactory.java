package com.allinfinance.dev.batch.scaffold.config;

import com.allinfinance.dev.batch.scaffold.util.ApplicationContextUtil;
import com.allinfinance.dev.core.util.common.BeanUtils;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * @author qipeng
 * @date 2022/2/13 9:09
 */
@Component
public class JobDtoMapperBeanFactory implements BeanFactoryAware {
    private static BeanFactory localBeanFactory;

    /**
     * 获取平面文件的记录行与DTO的映射对象：FieldSetMapper
     *
     * @param beanName 要注册的mapper名称
     * @param type     要生成的DTO类
     * @return FieldSetMapper
     */
    public FieldSetMapper getFieldSetMapper(String beanName, Class type) {
        BeanWrapperFieldSetMapper fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(type);
        fieldSetMapper.setBeanFactory(localBeanFactory);
        //此处bean名默认取类名首字母小写
        fieldSetMapper.setPrototypeBeanName(BeanUtils.getDefaultBeanName(type.getSimpleName()));
        return (FieldSetMapper) ApplicationContextUtil.registerSingletonBean(beanName, fieldSetMapper);
    }

    /**
     *
     * @param beanName
     * @param type
     * @return
     */
    public static RowMapper getJdbcRowMapper(String beanName, Class type) {
        BeanPropertyRowMapper beanPropertyRowMapper = new BeanPropertyRowMapper();

        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        localBeanFactory = beanFactory;
    }
}
