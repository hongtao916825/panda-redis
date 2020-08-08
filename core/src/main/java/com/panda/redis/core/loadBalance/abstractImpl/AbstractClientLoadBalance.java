package com.panda.redis.core.loadBalance.abstractImpl;


import com.panda.redis.base.api.Client;
import com.panda.redis.core.context.ServersContext;
import com.panda.redis.core.loadBalance.ClientLoadBalance;
import com.panda.redis.core.loadBalance.impl.RoundRobinClientLoadBalance;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractClientLoadBalance implements ClientLoadBalance {

    @Override
    public Client chooseClient(){
        return doChooseClient(ServersContext.get());
    }

    protected abstract Client doChooseClient(ServersContext serversContext);

    @Override
    public final ClientLoadBalance cloneClientLoadBalance(){
        ClientLoadBalance toClone = this;
        ClientLoadBalance rule;
        if (toClone == null) {
            rule = new RoundRobinClientLoadBalance();
        } else {
            String ruleClass = toClone.getClass().getName();
            try {
                Class clazz = Class.forName(ruleClass);
                rule = (ClientLoadBalance) clazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Unexpected exception creating rule for ZoneAwareLoadBalancer", e);
            }
        }
        return rule;
    }

}
