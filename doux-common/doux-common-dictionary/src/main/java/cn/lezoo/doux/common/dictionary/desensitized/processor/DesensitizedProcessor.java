package cn.lezoo.doux.common.dictionary.desensitized.processor;


import cn.lezoo.doux.common.dictionary.desensitized.DesensitizedType;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2021/7/3 23:03
 */
public interface DesensitizedProcessor {
    /**
     * 脱敏类型
     *
     * @return 脱敏类型
     */
    DesensitizedType supportType();

    /**
     * 脱敏方法
     *
     * @param object 待脱敏字符串
     * @return 脱敏后字符串
     */
    String desensitize(String object);
}
