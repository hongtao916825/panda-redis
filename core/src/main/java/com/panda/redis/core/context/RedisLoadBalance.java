package com.panda.redis.core.context;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.loadBalance.ClientLoadBalance;
import com.panda.redis.core.loadBalance.GroupLoadBalance;
import com.panda.redis.core.loadBalance.impl.KeyHashLoadBalance;
import com.panda.redis.core.loadBalance.impl.RoundRobinClientLoadBalance;
import com.panda.redis.core.properties.GroupClient;
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
        setValue(key, value);
//        serversContext.setKey(key);
//        serversContext.setValue(value);
        return jedisPool.getResource().set(key, value);
    }

    private void setValue(String key, String value) {
        ServersContext serversContext = ServersContext.get();
        if (serversContext == null) {
            serversContext = new ServersContext();
            ServersContext.put(serversContext);
        }
        serversContext.setKey(key);
        serversContext.setKey(value);
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
        setValue(key, null);
        return jedisPool.getResource().get(key);
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
        setValue(key, null);
//        serversContext.setKey(key);
        return jedisPool.getResource().get(key);
    }
}
