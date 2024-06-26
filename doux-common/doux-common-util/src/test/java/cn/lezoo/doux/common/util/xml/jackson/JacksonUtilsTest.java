package cn.lezoo.doux.common.util.xml.jackson;

import cn.lezoo.doux.common.util.serialize.JacksonUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import org.junit.jupiter.api.Test;

class JacksonUtilsTest {
    private final XmlMapper xmlMapper = (XmlMapper) XmlMapper.builder()
            .enable(MapperFeature.USE_STD_BEAN_NAMING)
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build()
            .registerModule(new KotlinModule.Builder().build())
            .registerModule(new JavaTimeModule());

    @Test
    void toJson() {
        TestDTO testDTO = new TestDTO()
                .setName("张三")
                .setAge(18)
                .setCamelCase("驼峰")
                .setUnderline_case("下划线")
                .setUPPER_UNDERLINE_CASE("大写下划线")
                .setSubTestDTO(
                        new SubTestDTO()
                                .setSubName("李四")
                                .setSubAge(16)
                );
        System.out.println(JacksonUtils.toJson(testDTO));
        System.out.println(JacksonUtils.toXml(testDTO));
    }

    @Test
    void fromJson() {
        String json = "{\n" +
                "  \"name\" : \"张三\",\n" +
                "  \"age\" : 18,\n" +
                "  \"camelCase\" : \"驼峰\",\n" +
                "  \"underline_case\" : \"下划线\",\n" +
                "  \"UPPER_UNDERLINE_CASE\" : \"大写下划线\",\n" +
                "  \"subTestDTO\" : {\n" +
                "    \"subName\" : \"李四\",\n" +
                "    \"subAge\" : 16\n" +
                "  }\n" +
                "}";
        System.out.println(JacksonUtils.fromJson(json, TestDTO.class));
    }

    @Test
    void xmlNode() {
        // xmlMapper.readTree()
    }
}