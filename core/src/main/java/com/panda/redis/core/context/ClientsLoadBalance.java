package com.panda.redis.core.context;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.loadBalance.LoadBalance;
import com.panda.redis.core.loadBalance.impl.KeyHashLoadBalance;

public class ClientsLoadBalance {

    private ServersContext serversContext;

    private LoadBalance loadBalance = new KeyHashLoadBalance();

    public ClientsLoadBalance() {
    }

    public ClientsLoadBalance(ServersContext serversContext) {
        this.serversContext = serversContext;
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
        Client client = loadBalance.chooseServer(serversContext);
        return client.set(key, value);
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
        Client client = loadBalance.chooseServer(serversContext);
        return client.get(key);
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
        Client client = loadBalance.chooseServer(serversContext);
        return client.get(key);
    }
}
