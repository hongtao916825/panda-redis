package com.panda.redis.core.loadBalance.impl;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.context.ServersContext;
import com.panda.redis.core.loadBalance.GroupLoadBalance;
import com.panda.redis.core.loadBalance.abstractImpl.AbstractGroupLoadBalance;
import com.panda.redis.core.properties.GroupClient;

import java.util.List;

public class KeyHashLoadBalance extends AbstractGroupLoadBalance {

    @Override
    public GroupClient doChooseGroupServer(ServersContext serversContext) {
        String key = serversContext.getKey();
        List<GroupClient> servers = serversContext.getGroupClients();
        int result = key.hashCode()&Integer.MAX_VALUE;
        int i = result % servers.size();
        return servers.get(i);
    }

}
