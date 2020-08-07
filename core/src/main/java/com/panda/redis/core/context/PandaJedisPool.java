package com.panda.redis.core.context;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.loadBalance.GroupLoadBalance;
import com.panda.redis.core.loadBalance.impl.KeyHashLoadBalance;
import com.panda.redis.core.properties.GroupClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolAbstract;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

/**
 * @author tao.hong
 */
@Component
public class PandaJedisPool extends JedisPool {

    private ServersContext serversContext;

    private GroupLoadBalance groupLoadBalance;

    public PandaJedisPool(){}

    public PandaJedisPool(JedisPoolConfig jedisPoolConfig){super(jedisPoolConfig);}

    /**
     * todo 并发问题
     * @return
     */
    private Client chooseClient() {
        GroupClient groupClient = groupLoadBalance.chooseGroupServer(serversContext);
        serversContext.setClients(groupClient.getClients());
        return groupClient.chooseClient(serversContext);
    }

    @Override
    public Jedis getResource() {
        return chooseClient();
    }

    @Override
    protected void returnBrokenResource(final Jedis resource) {
        if (resource != null) {
            returnBrokenResourceObject(resource);
        }
    }

    @Override
    protected void returnResource(final Jedis resource) {
        if (resource != null) {
            try {
                resource.resetState();
                returnResourceObject(resource);
            } catch (Exception e) {
                returnBrokenResource(resource);
                throw new JedisException("Resource is returned to the pool as broken", e);
            }
        }
    }

}
