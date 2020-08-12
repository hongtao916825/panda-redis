package com.panda.redis.core.context;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.address.ProxyLoaderContext;
import com.panda.redis.core.loadBalance.GroupLoadBalance;
import com.panda.redis.core.properties.GroupProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;

/**
 * @author tao.hong
 */
@Component
public class PandaJedisPool extends JedisPool {

    @Autowired
    private GroupLoadBalance groupLoadBalance;

//    private List<GroupProxy> groupClientLists;

    public PandaJedisPool(){}

    public PandaJedisPool(JedisPoolConfig jedisPoolConfig){super(jedisPoolConfig);}

    public GroupLoadBalance getGroupLoadBalance() {
        return groupLoadBalance;
    }

    public void setGroupLoadBalance(GroupLoadBalance groupLoadBalance) {
        this.groupLoadBalance = groupLoadBalance;
    }

    /**
     * todo 并发问题
     * @return
     */
    private Client chooseClient() {
        try {
            ServersContext.get().setGroupClients(ProxyLoaderContext.getGroupProxyList());
            GroupProxy groupClient = groupLoadBalance.chooseGroupServer();
            ServersContext.get().setClients(groupClient.getClients());
            return groupClient.chooseClient();
        }finally {
            ServersContext.remove();
        }
    }

    @Override
    public Jedis getResource() {
//        return chooseClient();
        return null;
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
