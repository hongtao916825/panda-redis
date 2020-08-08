package com.panda.redis.core.loadBalance;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.context.ServersContext;
import com.panda.redis.core.properties.GroupClient;

/**
 * 集群内Client的负载分流策略
 */
public interface ClientLoadBalance {

    Client chooseClient();

    ClientLoadBalance cloneClientLoadBalance();
}
