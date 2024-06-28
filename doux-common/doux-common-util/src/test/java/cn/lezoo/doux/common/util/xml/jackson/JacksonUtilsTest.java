package cn.lezoo.doux.common.util.xml.jackson;

import cn.lezoo.doux.common.util.serialize.JacksonUtils;
import org.junit.jupiter.api.Test;

class JacksonUtilsTest {
    @Test
    void toXml() {
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
                ).setTestEnum(TestEnum.TEST_2);
        System.out.println(JacksonUtils.toXml(testDTO));
    }

    @Test
    void fromXml() {
        String xml = "<TEST_DTO>\n" +
                "  <age>18</age>\n" +
                "  <camelCase>驼峰</camelCase>\n" +
                "  <underline_case>下划线</underline_case>\n" +
                "  <UPPER_UNDERLINE_CASE>大写下划线</UPPER_UNDERLINE_CASE>\n" +
                "  <subTestDTO>\n" +
                "    <subName>李四</subName>\n" +
                "    <subAge>16</subAge>\n" +
                "  </subTestDTO>\n" +
                "  <testEnum>02</testEnum>\n" +
                "  <namexxx>张三</namexxx>\n" +
                "  <CREATE_TIME>20240627115217</CREATE_TIME>\n" +
                "</TEST_DTO>";
        System.out.println(JacksonUtils.fromXml(xml, TestDTO.class));
    }

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
}