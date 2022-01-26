package com.allinfinance.dev.rpc.scaffold.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author qipeng
 * @date 2021/12/30 15:39
 */
@ConfigurationProperties(prefix = "com.allinfinance.rpc")
public class RpcConfigurationProperties {
    /**
     * 引用接口列表，以数组形式提供
     */
    private List<String> referenceList;
    /**
     * 服务提供方的包路径
     */
    private String providerPackage;

    public String getProviderPackage() {
        return providerPackage;
    }

    public void setProviderPackage(String providerPackage) {
        this.providerPackage = providerPackage;
    }

    public List<String> getReferenceList() {
        return referenceList;
    }

    public void setReferenceList(List<String> referenceList) {
        this.referenceList = referenceList;
    }
}
