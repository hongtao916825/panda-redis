package com.panda.redis.core.loadBalance;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.context.ServersContext;
import com.panda.redis.core.properties.GroupClient;

/**
 * 选取proxy组的负载分流策略，也就是选取集群
 */
public interface GroupLoadBalance {

    GroupClient chooseGroupServer(ServersContext serversContext);

}
