package com.panda.redis.core.loadBalance.impl;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.context.ServersContext;
import com.panda.redis.core.loadBalance.LoadBalance;

import java.util.List;

public class KeyHashLoadBalance implements LoadBalance {
    @Override
    public Client chooseServer(ServersContext serversContext) {
        String key = serversContext.getKey();
        List<Client> servers = serversContext.getServers();
        int result = key.hashCode()&Integer.MAX_VALUE;
        int i = result % servers.size();
        return servers.get(i);
    }

}
