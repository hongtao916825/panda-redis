package com.panda.redis.core.loadBalance.abstractImpl;

import com.panda.redis.core.context.ServersContext;
import com.panda.redis.core.loadBalance.GroupLoadBalance;
import com.panda.redis.core.properties.GroupProxy;

public abstract class AbstractGroupLoadBalance implements GroupLoadBalance {

    @Override
    public GroupProxy chooseGroupServer(){
        return this.doChooseGroupServer(ServersContext.get());
    }

    protected abstract GroupProxy doChooseGroupServer(ServersContext serversContext);

}
