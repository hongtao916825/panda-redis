package com.panda.redis.core.loadBalance;

import com.panda.redis.base.api.Client;

/**
 * 集群内Client的负载分流策略
 */
public interface ProxyLoadBalance {

    Client chooseProxy();

}
