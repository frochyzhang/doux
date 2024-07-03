package cn.lezoo.doux.common.hsp.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author huanghf
 * @date 2022/6/22 17:13
 */
@Data
@Accessors(chain = true)
public class HspBaseResponseDTO<T> implements Serializable {
    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 描述
     */
    private String desc;
    /**
     * 响应数据
     */
    private T response;

    public HspBaseResponseDTO() {
    }

    public HspBaseResponseDTO(Boolean success, String desc, T response) {
        this.success = success;
        this.desc = desc;
        this.response = response;
    }

    public static HspBaseResponseDTO<?> success() {
        return new HspBaseResponseDTO<>()
                .setSuccess(Boolean.TRUE);
    }

    public static <T> HspBaseResponseDTO<T> success(String msg, T response) {
        return new HspBaseResponseDTO<>(Boolean.TRUE, msg, response);
    }

    public static <T> HspBaseResponseDTO<T> fail(String msg) {
        return new HspBaseResponseDTO<>(Boolean.FALSE, msg, null);
    }
}
