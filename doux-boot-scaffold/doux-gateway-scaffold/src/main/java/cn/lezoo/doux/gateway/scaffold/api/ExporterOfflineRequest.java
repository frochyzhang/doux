package cn.lezoo.doux.gateway.scaffold.api;

import java.io.Serializable;

/**
 * @author huanghf
 * @date 2022/11/29 19:52
 */
public class ExporterOfflineRequest implements Serializable {
    private String appUniqueId;

    public ExporterOfflineRequest(String appUniqueId) {
        this.appUniqueId = appUniqueId;
    }

    public String getAppUniqueId() {
        return appUniqueId;
    }

    public void setAppUniqueId(String appUniqueId) {
        this.appUniqueId = appUniqueId;
    }

    @Override
    public String toString() {
        return "ExporterOfflineRequest{" +
                "appUniqueId='" + appUniqueId + '\'' +
                '}';
    }
}
