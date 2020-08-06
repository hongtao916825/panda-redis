package com.panda.redis.core.loadBalance;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.context.ServersContext;

public interface LoadBalance {

    Client chooseServer(ServersContext serversContext);

}
