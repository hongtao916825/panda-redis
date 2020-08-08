package com.panda.redis.core.loadBalance.impl;

import com.panda.redis.core.context.ServersContext;
import com.panda.redis.core.loadBalance.abstractImpl.AbstractGroupLoadBalance;
import com.panda.redis.core.properties.GroupProxy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeyHashLoadBalance extends AbstractGroupLoadBalance {

    @Override
    public GroupProxy doChooseGroupServer(ServersContext serversContext) {
        String key = serversContext.getKey();
        List<GroupProxy> servers = serversContext.getGroupClients();
        int result = key.hashCode()&Integer.MAX_VALUE;
        int i = result % servers.size();
        return servers.get(i);
    }

}
