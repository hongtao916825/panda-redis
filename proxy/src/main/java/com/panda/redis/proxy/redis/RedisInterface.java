package com.panda.redis.proxy.redis;


import com.panda.redis.base.protocol.RedisCommand;
import com.panda.redis.proxy.config.ProxyPool;

import java.lang.reflect.Proxy;

public interface RedisInterface {

    byte[] send(RedisCommand cmd);

    RedisInterface setResource(ProxyPool proxyPool);

}
