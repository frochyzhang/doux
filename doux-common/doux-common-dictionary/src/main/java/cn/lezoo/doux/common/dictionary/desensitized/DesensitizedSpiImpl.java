package cn.lezoo.doux.common.dictionary.desensitized;

import cn.lezoo.doux.common.dictionary.logger.DesensitizedSpi;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2021/7/3 02:20
 */
@Component
public class DesensitizedSpiImpl implements DesensitizedSpi, InitializingBean {
    private DesensitizedHelper desensitizedHelper;


    /**
     * 数据脱敏
     *
     * @param data 待脱敏数据
     * @return 脱敏后数据
     */
    @Override
    public String convert(Object data) {
        return desensitizedHelper == null ? String.valueOf(data) : desensitizedHelper.toString(data);
    }

    @Override
    public void afterPropertiesSet() {
        if (desensitizedHelper == null) {
            throw new IllegalArgumentException("脱敏服务注入失败，请检查!");
        }
    }

    @Autowired
    public void setDesensitizedHelper(DesensitizedHelper desensitizedHelper) {
        this.desensitizedHelper = desensitizedHelper;
    }
}
