package com.panda.redis.core.context;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.loadBalance.ClientLoadBalance;
import com.panda.redis.core.properties.GroupClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServersContext {

//    private Map<String, ClientLoadBalance> clientLoadBalanceMap = new ConcurrentHashMap<>();

    private List<GroupClient> groupClients;

    private String key;

    private String value;

    private List<Client> Clients;

    public ServersContext() {
    }

    public ServersContext(List<GroupClient> servers) {
        this.groupClients = servers;
    }

    public ServersContext(List<GroupClient> groupClients, String key, String value) {
        this.groupClients = groupClients;
        this.key = key;
        this.value = value;
    }

    public List<GroupClient> getGroupClients() {
        return groupClients;
    }

    public void setGroupClients(List<GroupClient> groupClients) {
        this.groupClients = groupClients;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Client> getClients() {
        return Clients;
    }

    public void setClients(List<Client> clients) {
        Clients = clients;
    }

//    public void register(GroupClient groupClient, ClientLoadBalance clientLoadBalance){
//        this.clientLoadBalanceMap.put(groupClient.getId(), clientLoadBalance);
//    }
//
//    public ClientLoadBalance getClientLoadBalance(GroupClient groupClient){
//        return this.clientLoadBalanceMap.get(groupClient.getId());
//    }

}
