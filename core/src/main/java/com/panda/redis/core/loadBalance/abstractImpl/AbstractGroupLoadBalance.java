package com.panda.redis.core.loadBalance.abstractImpl;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.context.ServersContext;
import com.panda.redis.core.loadBalance.ClientLoadBalance;
import com.panda.redis.core.loadBalance.GroupLoadBalance;
import com.panda.redis.core.properties.GroupClient;

public abstract class AbstractGroupLoadBalance implements GroupLoadBalance {

    @Override
    public GroupClient chooseGroupServer(){
        return this.doChooseGroupServer(ServersContext.get());
    }

    protected abstract GroupClient doChooseGroupServer(ServersContext serversContext);

}
