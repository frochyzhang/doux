package cn.lezoo.doux.common.util.xml.jackson;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author huanghf
 * @date 2024/6/26 13:55
 */
@Data
@Accessors(chain = true)
@JacksonXmlRootElement(localName = "TEST_DTO")
public class TestDTO {
    private String name;
    private Integer age;
    private String camelCase;
    private String underline_case;
    private String UPPER_UNDERLINE_CASE;
    private SubTestDTO subTestDTO;
}
