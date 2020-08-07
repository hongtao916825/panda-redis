package com.panda.redis.core.loadBalance.abstractImpl;


import com.panda.redis.base.api.Client;
import com.panda.redis.core.context.ServersContext;
import com.panda.redis.core.loadBalance.ClientLoadBalance;

public abstract class AbstractClientLoadBalance implements ClientLoadBalance {

    @Override
    public Client chooseClient(ServersContext serversContext){
        return doChooseClient(serversContext);
    }

    protected abstract Client doChooseClient(ServersContext serversContext);

}
