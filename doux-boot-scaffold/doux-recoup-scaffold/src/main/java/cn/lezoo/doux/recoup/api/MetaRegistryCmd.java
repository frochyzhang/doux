package cn.lezoo.doux.recoup.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2024/1/23 15:24
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MetaRegistryCmd {
    private String host;
    private Integer port;
    private String appName;
    private MetaData metaData;


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class MetaData {
        private String className;
        private String data;
    }
}
