package cn.lezoo.doux.common.util.xml.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author huanghf
 * @date 2024/6/26 13:55
 */
@Data
@Accessors(chain = true)
@JacksonXmlRootElement(localName = "TEST_DTO")
public class TestDTO {
    @JsonProperty("namexxx")
    private String name;
    private Integer age;
    private String camelCase;
    private String underline_case;
    private String UPPER_UNDERLINE_CASE;
    private SubTestDTO subTestDTO;
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    @JacksonXmlProperty(localName = "CREATE_TIME")
    private LocalDateTime createTime = LocalDateTime.now();
    private TestEnum testEnum;
}
