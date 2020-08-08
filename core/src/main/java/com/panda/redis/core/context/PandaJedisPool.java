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

import java.util.List;

/**
 * @author tao.hong
 */
@Component
public class PandaJedisPool extends JedisPool {

    @Autowired
    private GroupLoadBalance groupLoadBalance;

    private List<GroupClient> groupClientLists;

    public PandaJedisPool(){}

    public PandaJedisPool(JedisPoolConfig jedisPoolConfig,List<GroupClient> groupClientLists){super(jedisPoolConfig);this.groupClientLists = groupClientLists;}

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
            ServersContext.get().setGroupClients(groupClientLists);
            GroupClient groupClient = groupLoadBalance.chooseGroupServer();
            ServersContext.get().setClients(groupClient.getClients());
            return groupClient.chooseClient();
        }finally {
            ServersContext.remove();
        }
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
