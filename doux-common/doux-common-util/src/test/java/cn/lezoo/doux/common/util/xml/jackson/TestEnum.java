package cn.lezoo.doux.common.util.xml.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum TestEnum {
    TEST_1("01", "test01"),
    TEST_2("02", "test02");

    @JsonValue
    private final String code;
    private final String desc;

    TestEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonCreator
    public static TestEnum fromCode(String code) {
        return Stream.of(values())
                .filter(testEnum -> testEnum.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("未查找到对应枚举"));
    }
}
