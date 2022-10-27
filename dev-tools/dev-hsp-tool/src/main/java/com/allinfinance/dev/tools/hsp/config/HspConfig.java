package com.allinfinance.dev.tools.hsp.config;

/**
 * @author qipeng
 * @date 2022/10/27 10:09
 * @desc
 */
@Component
@NacosConfigurationProperties(groupId = Constants.DEFAULT_GROUP, type = ConfigType.YAML,
        dataId = "dev-hsp-tool", prefix = "dev.hsp.tool", autoRefreshed = true)
public class HspConfig {

}
