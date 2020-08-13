package com.panda.redis.proxy.redis.impl;

import com.panda.redis.base.common.LogUtil;
import com.panda.redis.proxy.redis.AbstractRedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.Protocol;

public class RedisClusterImplement extends AbstractRedis {
    @Override
    protected Object get(Protocol.Command get, byte[] arg1) {
        JedisCluster jedisCluster = null;
        Object o = null;
        try {
            jedisCluster = proxyPool.getJedisCluster();
            o = jedisCluster.sendCommand(arg1, get);
        } catch (Exception e){
            LogUtil.error("send command error:",e);
        } finally {
            if(jedisCluster != null){
                jedisCluster.close();
            }
        }
        return o;
    }

    @Override
    protected Object set(Protocol.Command set, byte[] arg1, byte[] arg2) {
        JedisCluster jedisCluster = null;
        Object o = null;
        try {
            jedisCluster = proxyPool.getJedisCluster();
            o = jedisCluster.sendCommand(arg1, set, arg2);
        } catch (Exception e){
            LogUtil.error("send command error:",e);
        } finally {
            if(jedisCluster != null){
                jedisCluster.close();
            }
        }
        return o;
    }
}
