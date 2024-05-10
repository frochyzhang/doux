package cn.lezoo.doux.white.list.diversion.socket.backup;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author huanghf
 * @date 2024/3/26 15:28
 */
public enum TradeDirection {
    IN("I", "请求"),
    OUT("O", "响应");

    @JsonValue
    private final String direction;
    private final String desc;

    TradeDirection(String direction, String desc) {
        this.direction = direction;
        this.desc = desc;
    }

    public String getDirection() {
        return direction;
    }

    public String getDesc() {
        return desc;
    }
}
