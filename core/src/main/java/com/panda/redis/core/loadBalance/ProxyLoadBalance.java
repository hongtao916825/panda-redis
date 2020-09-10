package com.panda.redis.core.loadBalance;

import com.panda.redis.base.api.Client;
import redis.clients.jedis.Jedis;

/**
 * 集群内Client的负载分流策略
 */
public interface ProxyLoadBalance {

    String chooseProxy();

}
