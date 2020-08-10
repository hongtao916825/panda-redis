package com.panda.redis.base.api;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.util.Pool;

/**
 * @author tao.hong
 */
@Component
public class PandaJedisPool extends Pool<Client> {


    public PandaJedisPool(){}

    public PandaJedisPool(final GenericObjectPoolConfig poolConfig, final String host, final int port) {
        this(poolConfig, host, port, Protocol.DEFAULT_TIMEOUT);
    }


    public PandaJedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int defaultTimeout) {
        super(poolConfig, new ClientFactory(host, port, Protocol.DEFAULT_TIMEOUT, Protocol.DEFAULT_TIMEOUT, null,
                Protocol.DEFAULT_DATABASE, null));
    };

    @Override
    public Client getResource() {
        return super.getResource();
    }

    @Override
    protected void returnBrokenResource(final Client resource) {
        if (resource != null) {
            returnBrokenResourceObject(resource);
        }
    }

    @Override
    protected void returnResource(final Client resource) {
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
