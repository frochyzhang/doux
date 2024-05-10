package cn.lezoo.doux.common.dictionary.desensitized.processor;

import cn.lezoo.doux.common.dictionary.desensitized.DesensitizedType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2021/7/3 23:15
 */
@Component
public class AddressProcessor extends BaseProcessor {
    private static final Pattern PATTERN = Pattern.compile("[0-9]");

    /**
     * 脱敏类型
     *
     * @return 脱敏类型
     */
    @Override
    public DesensitizedType supportType() {
        return DesensitizedType.ADDRESS;
    }

    /**
     * 脱敏方法
     *
     * @param object 待脱敏字符串
     * @return 脱敏后字符串
     */
    @Override
    public String desensitize(String object) {
        if (StringUtils.isBlank(object)) {
            return null;
        } else {
            Matcher m = PATTERN.matcher(object);
            return m.replaceAll("*").trim();
        }
    }
}
