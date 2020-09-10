package com.panda.redis.core.loadBalance.abstractImpl;


import com.panda.redis.base.api.Client;
import com.panda.redis.core.context.ServersContext;
import com.panda.redis.core.loadBalance.ProxyLoadBalance;
import redis.clients.jedis.Jedis;

public abstract class AbstractProxyLoadBalance implements ProxyLoadBalance {

    @Override
    public String chooseProxy(){
        return doChooseProxy(ServersContext.get());
    }

    protected abstract String doChooseProxy(ServersContext serversContext);


}
