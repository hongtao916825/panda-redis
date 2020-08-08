package com.panda.redis.core.loadBalance.abstractImpl;


import com.panda.redis.base.api.Client;
import com.panda.redis.core.context.ServersContext;
import com.panda.redis.core.loadBalance.ProxyLoadBalance;

public abstract class AbstractProxyLoadBalance implements ProxyLoadBalance {

    @Override
    public Client chooseProxy(){
        return doChooseProxy(ServersContext.get());
    }

    protected abstract Client doChooseProxy(ServersContext serversContext);


}
