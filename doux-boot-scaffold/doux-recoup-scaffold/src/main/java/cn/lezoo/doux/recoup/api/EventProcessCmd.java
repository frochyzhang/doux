package cn.lezoo.doux.recoup.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2024/1/23 16:51
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EventProcessCmd {
    private String className;
    private String data;
}
