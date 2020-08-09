package com.panda.redis.core.address;

import com.panda.redis.core.properties.GroupProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hongtao
 */
public class ProxyLoaderContext {

    private static Map<String, String> proxyLoaderMap = new ConcurrentHashMap<>(10);

    public static List<GroupProxy> groupProxyList = new ArrayList<>();

    static {
        proxyLoaderMap.put("zookeeper:", "com.panda.redis.core.address.zkImpl.ZookeeperProxyRegisterImpl");
    }

    public static String getProxyRegisterByRegisterAddress(String address){
        String prefix = address.substring(0, address.indexOf(":")+1);
        return proxyLoaderMap.get(prefix);
    }

}
