package com.panda.redis.core.context;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.loadBalance.ClientLoadBalance;
import com.panda.redis.core.loadBalance.GroupLoadBalance;
import com.panda.redis.core.loadBalance.impl.KeyHashLoadBalance;
import com.panda.redis.core.loadBalance.impl.RoundRobinClientLoadBalance;
import com.panda.redis.core.properties.GroupClient;

public class LoadBalance {

    private ServersContext serversContext;

    private GroupLoadBalance grouploadBalance = new KeyHashLoadBalance();


    public LoadBalance() {
    }

    public LoadBalance(ServersContext serversContext) {
        this.serversContext = serversContext;
        serversContext.getGroupClients().stream().forEach(k -> serversContext.register(k, new RoundRobinClientLoadBalance()));
    }

    /**
     * todo 并发问题
     * @return
     */
    private Client chooseClient() {
        GroupClient groupClient = grouploadBalance.chooseGroupServer(serversContext);
        serversContext.setClients(groupClient.getClients());
        return serversContext.getClientLoadBalance(groupClient).chooseClient(serversContext);
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
        serversContext.setKey(key);
        serversContext.setValue(value);
        return chooseClient().set(key, value);
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
        serversContext.setKey(key);
        return chooseClient().get(key);
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
        serversContext.setKey(key);
        return chooseClient().get(key);
    }
}
