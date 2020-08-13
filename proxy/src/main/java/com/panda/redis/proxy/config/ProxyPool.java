package com.panda.redis.proxy.config;

import com.google.common.collect.ImmutableMap;
import com.panda.redis.base.constants.ProxyConstants;
import com.panda.redis.base.protocol.RedisCommand;
import com.panda.redis.proxy.redis.RedisInterface;
import com.panda.redis.proxy.redis.impl.RedisClusterImplement;
import com.panda.redis.proxy.redis.impl.RedisSentinelImplement;
import com.panda.redis.proxy.redis.impl.RedisSingleImplement;
import jdk.nashorn.internal.ir.annotations.Immutable;
import redis.clients.jedis.*;

import java.util.HashMap;
import java.util.Map;

public class ProxyPool {

    private static Map<String, RedisInterface> RedisImplementMap;

    static {
        RedisImplementMap = ImmutableMap.<String, RedisInterface>builder()
                .put(ProxyConstants.CLUSTER_SINGLE, new RedisSingleImplement())
                .put(ProxyConstants.CLUSTER_SENTINEL, new RedisSentinelImplement())
                .put(ProxyConstants.CLUSTER_CLUSTERE, new RedisClusterImplement())
                .build();

    }

    private JedisPool jedisPool;

    private JedisCluster jedisCluster;

    private JedisSentinelPool jedisSentinelPool;

    private String cluster;

    public ProxyPool() {
    }

    public ProxyPool(String cluster) {
        this.cluster = cluster;
    }

    public ProxyPool(JedisPool jedisPool, JedisCluster jedisCluster, JedisSentinelPool jedisSentinelPool) {
        this.jedisPool = jedisPool;
        this.jedisCluster = jedisCluster;
        this.jedisSentinelPool = jedisSentinelPool;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    public JedisSentinelPool getJedisSentinelPool() {
        return jedisSentinelPool;
    }

    public void setJedisSentinelPool(JedisSentinelPool jedisSentinelPool) {
        this.jedisSentinelPool = jedisSentinelPool;
    }

    public byte[] send(RedisCommand cmd) {
        return RedisImplementMap.get(cluster).setResource(this).send(cmd);
    }

}
