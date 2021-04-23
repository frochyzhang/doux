package com.allinfinance.dev.nds.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classname  com.allinfinance.dev.nds.resp.NDSResponse
 *
 * @Description TODO
 * @Date 2021/4/2 16:14
 * @Created by ZhangYong
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NDSResponse {
    private Boolean status;
    private String message;
    private Object data;


    public static NDSResponse ok(Object data) {
        return new NDSResponse(Boolean.TRUE, "交易处理成功", data);
    }

    public static NDSResponse failure() {
        return new NDSResponse(Boolean.FALSE, "交易处理失败", new Object());
    }
}
