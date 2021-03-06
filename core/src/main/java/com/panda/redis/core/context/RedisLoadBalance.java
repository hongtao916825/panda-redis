package com.panda.redis.core.context;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisLoadBalance {

    private JedisPool jedisPool;

    public RedisLoadBalance(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public RedisLoadBalance() {
    }


    /**
     * Set the string value as value of the key. The string can't be longer than 1073741824 bytes (1
     * GB).
     * <p>
     * Time complexity: O(1)
     * @param key
     * @param value
     * @return Status code reply
     */
    public String set(final String key, String value) {
        Jedis jedis = null;
        try {
            setValue(key, value);
            jedis = jedisPool.getResource();
            return jedisPool.getResource().set(key, value);
        } finally {
            if(jedis != null){jedis.close();}
        }
    }

    private void setValue(String key, String value) {
        ServersContext serversContext = ServersContext.get();
        if (serversContext == null) {
            serversContext = new ServersContext();
            ServersContext.put(serversContext);
        }
        serversContext.setKey(key);
        serversContext.setValue(value);
    }


    /**
     * Get the value of the specified key. If the key does not exist null is returned. If the value
     * stored at key is not a string an error is returned because GET can only handle string values.
     * <p>
     * Time complexity: O(1)
     * @param key
     * @return Bulk reply
     */
    public String get(final String key) {
        Jedis jedis = null;
        try {
            setValue(key, null);
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } finally {
            if(jedis != null){jedis.close();}
        }

    }


    /**
     * Get the value of the specified key. If the key does not exist null is returned. If the value
     * stored at key is not a string an error is returned because GET can only handle string values.
     * <p>
     * Time complexity: O(1)
     * @param key
     * @return Bulk reply
     */
    public String ping(final String key) {
        Jedis jedis = null;
        setValue(key, null);
        try {
            jedis = jedisPool.getResource();
            return jedis.ping(key);
        } finally {
            if(jedis != null){jedis.close();}
        }
    }
}
