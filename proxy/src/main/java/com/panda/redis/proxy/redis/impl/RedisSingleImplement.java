package com.panda.redis.proxy.redis.impl;

import com.panda.redis.base.common.LogUtil;
import com.panda.redis.proxy.redis.AbstractRedis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

public class RedisSingleImplement extends AbstractRedis {

    @Override
    protected Object get(Protocol.Command get, byte[] arg1) {
        Jedis jedis = null;
        Object o = null;
        try {
            jedis = proxyPool.getJedisPool().getResource();
            o = jedis.sendCommand(get, arg1);
        } catch (Exception e) {
            LogUtil.error("sendCommand error：", e);
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return o;
    }

    @Override
    protected Object set(Protocol.Command set, byte[] arg1, byte[] arg2) {
        Jedis jedis = null;
        Object o = null;
        try {
            jedis = proxyPool.getJedisPool().getResource();
            o = jedis.sendCommand(set, arg1, arg2);
        } catch (Exception e) {
            LogUtil.error("sendCommand error：", e);
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return o;
    }

}
