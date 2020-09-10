package com.panda.redis.core.pojo;

import com.panda.redis.core.loadBalance.ProxyLoadBalance;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.util.*;

public class GroupProxy {

    private JedisPoolConfig jedisPoolConfig;

    private String id;

    private Map<String, JedisPool> jedisPools;

    private ProxyLoadBalance proxyLoadBalanceRule;

    private Set<String> proxyList;

    private GroupProxy() {
    }

    public GroupProxy(String id, JedisPoolConfig jedisPoolConfig, Set<String> proxyList, ProxyLoadBalance proxyLoadBalance) {
        this.id = id;
        this.jedisPoolConfig = jedisPoolConfig;
        this.proxyList = proxyList;
        this.proxyLoadBalanceRule = proxyLoadBalance;
        jedisPools = new HashMap<>();
    }

    public GroupProxy(String id, JedisPoolConfig jedisPoolConfig, ProxyLoadBalance proxyLoadBalance) {
        this(id, jedisPoolConfig, new HashSet<>(), proxyLoadBalance);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, JedisPool> getJedisPools() {
        return jedisPools;
    }

    public void setJedisPools(Map<String, JedisPool> jedisPools) {
        this.jedisPools = jedisPools;
    }

    public ProxyLoadBalance getProxyLoadBalanceRule() {
        return proxyLoadBalanceRule;
    }

    public void setProxyLoadBalanceRule(ProxyLoadBalance proxyLoadBalanceRule) {
        this.proxyLoadBalanceRule = proxyLoadBalanceRule;
    }

    public Set<String> getProxyList() {
        return proxyList;
    }

    public void setProxyList(Set<String> proxyList) {
        this.proxyList = proxyList;
    }

    /**
     * 全量更新
     */
    public void refreshProxies() {
        synchronized (this){
            this.getJedisPools().clear();
            if(!CollectionUtils.isEmpty(proxyList)) {
                proxyList.forEach(proxy ->
                        this.jedisPools.put(proxy, new JedisPool(jedisPoolConfig,
                                proxy.split(":")[0],
                                Integer.valueOf(proxy.split(":")[1])
                                , Protocol.DEFAULT_TIMEOUT))

                );
            }
        }
    }

    /**
     * 增量更新
     * @param address
     * @return
     */
    public void addProxy(String address){
        if(!this.containsProxy(address)) {
            this.jedisPools.put(address, new JedisPool(jedisPoolConfig,
                    address.split(":")[0],
                    Integer.valueOf(address.split(":")[1])
                    , Protocol.DEFAULT_TIMEOUT));
            this.proxyList.add(address);
        }
    }


    public boolean containsProxy(String address){
        return this.proxyList.contains(address);
    }

    public String chooseClient() {
        return proxyLoadBalanceRule.chooseProxy();
    }

    public void removeProxies(String address) {
        if(this.containsProxy(address)) {
            this.jedisPools.remove(address);
            this.proxyList.remove(address);
        }
    }

    //    private String id;
//
//    private List<Client> clients;
//
//    public GroupProxy(){
//        // 每一个集群生成一个id
//        id = StringUtils.isEmpty(id)? UUID.randomUUID().toString() : id;
//        clients = new ArrayList<>();
//    }
//
//    public GroupProxy(String id){
//        // 每一个集群生成一个id
//        clients = new ArrayList<>();
//    }
//
//    public List<Client> getClients() {
//        return clients;
//    }
//
//    public void setClients(List<Client> clients) {
//        this.clients = clients;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public GroupProxy addClients(List<Client> clients) {
//        this.clients.addAll(clients);
//        return this;
//    }
//
//    public GroupProxy addClient(Client client) {
//        this.clients.add(client);
//        return this;
//    }
//
//    public ProxyLoadBalance getProxyLoadBalanceRule() {
//        return proxyLoadBalanceRule;
//    }
//
//    public void setProxyLoadBalanceRule(ProxyLoadBalance clientLoadBalance) {
//        this.proxyLoadBalanceRule = clientLoadBalance;
//    }
//
//    public Client chooseClient() {
//        return proxyLoadBalanceRule.chooseProxy();
//    }
//
//    public void reflushClients(List<String> childPath,String path) {
//        synchronized (this){
//            this.getClients().clear();
////            if(childPath.isEmpty()){
////                // 没有proxy，剔除集群
////                ProxyLoaderContext.removeGroup(path);
////            }
//            childPath.stream().map(Client::new).forEach(this.getClients()::add);
//        }
//    }
}
