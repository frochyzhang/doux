package cn.lezoo.doux.common.util.xml.jackson;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author huanghf
 * @date 2024/6/26 14:08
 */
@Data
@Accessors(chain = true)
public class SubTestDTO {
    private String subName;
    private int subAge;
}
