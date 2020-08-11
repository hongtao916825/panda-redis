package com.panda.redis.core.address.zkImpl;

import java.util.HashSet;
import java.util.Set;

public class ZookeeperContext {

    // 监听列表
    private final static Set<String> REGISTED_PATH = new HashSet<>(100);

    public static void addToRegister(String path){
        REGISTED_PATH.add(path);
    }

    public static Boolean hasRegistered(String path){
        return REGISTED_PATH.contains(path);
    }

    public static void removeRegistered(String path){
        REGISTED_PATH.remove(path);
    }

}
