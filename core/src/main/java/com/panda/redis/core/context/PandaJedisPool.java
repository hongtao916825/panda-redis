package com.panda.redis.core.context;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.address.ProxyLoaderContext;
import com.panda.redis.core.loadBalance.GroupLoadBalance;
import com.panda.redis.core.pojo.GroupProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

/**
 * @author tao.hong
 */
@Component
public class PandaJedisPool extends JedisPool {

    @Autowired
    private GroupLoadBalance groupLoadBalance;

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
    private Jedis chooseClient() {
        Assert.notEmpty(ProxyLoaderContext.getGroupProxyList(), "no server avliable");
        ServersContext.get().setGroupClients(ProxyLoaderContext.getGroupProxyList());
        GroupProxy groupProxy = groupLoadBalance.chooseGroupServer();
        ServersContext.get().setProxies(groupProxy.getJedisPools().keySet());
        String proxyAddress = groupProxy.chooseClient();
        JedisPool jedisPool = groupProxy.getJedisPools().get(proxyAddress);
        Assert.notNull(jedisPool, "proxyAddress can not be found in this group");
        return jedisPool.getResource();
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
