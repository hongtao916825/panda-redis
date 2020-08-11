package com.panda.redis.core.address;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.loadBalance.ProxyLoadBalance;
import com.panda.redis.core.properties.GroupProxy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author hongtao
 */
public class ProxyLoaderContext {

    private static Map<String, String> proxyLoaderMap = new ConcurrentHashMap<>(10);

    private static ReentrantLock lock = new ReentrantLock();

    private static List<GroupProxy> groupProxyList = new ArrayList<>();

    public static Map<String, GroupProxy> groupProxyMap = new ConcurrentHashMap<>();

    static {
        proxyLoaderMap.put("zookeeper:", "com.panda.redis.core.address.zkImpl.ZookeeperProxyRegisterImpl");
    }

    public static String getProxyRegisterByRegisterAddress(String address){
        String prefix = address.substring(0, address.indexOf(":")+1);
        return proxyLoaderMap.get(prefix);
    }

    public static List<GroupProxy> getGroupProxyList(){
        return groupProxyList;
    }

    public static void reflushProxyList() {
        lock.lock();
        try {
            groupProxyList = null;
            groupProxyList = groupProxyMap.values().stream()
                    .filter(k -> !k.getClients().isEmpty()).collect(Collectors.toList());
        }finally {
            lock.unlock();
        }
    }

    public static void removeGroup(String path) {
        lock.lock();
        try {
            proxyLoaderMap.remove(path);
            reflushProxyList();
        }finally {
            lock.unlock();
        }
    }

    /**
     *
     * @param groupPath 集群
     * @param clientPaths 代理
     * @param proxyLoadBalance 负载均衡
     */
    public static void addGroup(String groupPath, List<String> clientPaths, ProxyLoadBalance proxyLoadBalance) {
        if(!clientPaths.isEmpty()){
            List<Client> clients = clientPaths.stream().map(Client::new).collect(Collectors.toList());
            GroupProxy groupProxy = new GroupProxy();
            groupProxy.addClients(clients);
            groupProxy.setProxyLoadBalanceRule(proxyLoadBalance);
            groupProxyMap.put(groupPath, groupProxy);
            reflushProxyList();
        }
    }
}
