package com.allinfinance.dev.rpc.scaffold.consumer;

import com.alipay.sofa.rpc.boot.runtime.param.BoltBindingParam;
import com.alipay.sofa.runtime.api.aware.ClientFactoryAware;
import com.alipay.sofa.runtime.api.client.ClientFactory;
import com.alipay.sofa.runtime.api.client.ReferenceClient;
import com.alipay.sofa.runtime.api.client.param.ReferenceParam;
import org.springframework.stereotype.Component;

/**
 * @author huanghf
 * @date 2022/5/10 16:22
 */
@Component
public class ReferenceFactoryAware implements ClientFactoryAware {

    private ReferenceClient referenceClient;

    /**
     * Set the instance of {@link ClientFactory} to the Spring bean that implement this interface.
     *
     * @param clientFactory ClientFactory The instance of {@link ClientFactory}
     */
    @Override
    public void setClientFactory(ClientFactory clientFactory) {
        this.referenceClient = clientFactory.getClient(ReferenceClient.class);
    }

    public <T> T getReference(String providerUniqueId, Class<T> tClass, int timeout) {
        ReferenceParam<T> referenceParam = new ReferenceParam<>();
        BoltBindingParam boltBindingParam = new BoltBindingParam();
        boltBindingParam.setLoadBalancer("roundRobin");
        boltBindingParam.setTimeout(timeout);
        referenceParam.setBindingParam(boltBindingParam);
        referenceParam.setInterfaceType(tClass);
        referenceParam.setUniqueId(providerUniqueId);
        return referenceClient.reference(referenceParam);
    }
}
