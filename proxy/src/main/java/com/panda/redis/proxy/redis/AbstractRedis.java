package com.panda.redis.proxy.redis;

import com.panda.redis.base.protocol.BulkReply;
import com.panda.redis.base.protocol.Command;
import com.panda.redis.base.protocol.RedisCommand;
import com.panda.redis.proxy.config.ProxyPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;

public abstract class AbstractRedis implements RedisInterface{

    protected ProxyPool proxyPool;

    @Override
    public byte[] send(RedisCommand cmd){
//        Jedis jedis = proxyPool.getResource();
        try {
            if (cmd.getName().equalsIgnoreCase("set")) {
                Object o = set(Protocol.Command.SET, cmd.getArg1(), cmd.getArg2());
                return (byte[])o;
            }
            else if (cmd.getName().equalsIgnoreCase("get")) {
                Object o = get(Protocol.Command.GET, cmd.getArg1());
                return (byte[])o;
            }
            throw new RuntimeException("Command not supported");
        } catch (Exception e){
            //将内容返回到客户端
            throw new RuntimeException("sendCommand error", e);
        }

    }

    @Override
    public RedisInterface setResource(ProxyPool proxyPool){
         this.proxyPool = proxyPool;
         return this;
    }

    protected ProxyPool getReource(){
        return proxyPool;
    }


    protected abstract Object get(Protocol.Command get, byte[] arg1);

    protected abstract Object set(Protocol.Command set, byte[] arg1, byte[] arg2);

}
