package com.panda.redis.core.context;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.properties.GroupProxy;

import java.util.Collection;
import java.util.List;

public class ServersContext {
    private static ThreadLocal<ServersContext> threadLocal = new ThreadLocal<>();

//    private Map<String, ClientLoadBalance> clientLoadBalanceMap = new ConcurrentHashMap<>();

    private List<GroupProxy> groupClients;

    private String key;

    private String value;

    private List<Client> Clients;

    public ServersContext() {
    }

    public ServersContext(List<GroupProxy> servers) {
        this.groupClients = servers;
    }

    public ServersContext(List<GroupProxy> groupClients, String key, String value) {
        this.groupClients = groupClients;
        this.key = key;
        this.value = value;
    }

    public List<GroupProxy> getGroupClients() {
        return groupClients;
    }

    public void setGroupClients(List<GroupProxy> groupClients) {
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

    public static void put(ServersContext serversContext){
        threadLocal.set(serversContext);
    }

    public static ServersContext get(){
        return threadLocal.get();
    }

    public static void remove(){
        threadLocal.remove();
    }

//    public void register(GroupClient groupClient, ClientLoadBalance clientLoadBalance){
//        this.clientLoadBalanceMap.put(groupClient.getId(), clientLoadBalance);
//    }
//
//    public ClientLoadBalance getClientLoadBalance(GroupClient groupClient){
//        return this.clientLoadBalanceMap.get(groupClient.getId());
//    }

}
