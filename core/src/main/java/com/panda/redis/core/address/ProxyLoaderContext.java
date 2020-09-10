package com.panda.redis.core.address;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.loadBalance.ProxyLoadBalance;
import com.panda.redis.core.pojo.GroupProxy;

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

    public static void refreshGroupList() {
        lock.lock();
        try {
            groupProxyList = null;
            groupProxyList = groupProxyMap.values().stream()
                    .filter(k -> !k.getJedisPools().isEmpty()).collect(Collectors.toList());
        }finally {
            lock.unlock();
        }
    }

    public static void removeGroup(String groupId) {
        lock.lock();
        try {
            proxyLoaderMap.remove(groupId);
            refreshGroupList();
        }finally {
            lock.unlock();
        }
    }

//    /**
//     *
//     * @param groupPath 集群
//     * @param proxies 代理
//     * @param proxyLoadBalance 负载均衡
//     * @param groupId 集群id
//     */
//    public static void addGroup(String groupPath, List<String> proxies, ProxyLoadBalance proxyLoadBalance, String groupId) {
//    }
}
