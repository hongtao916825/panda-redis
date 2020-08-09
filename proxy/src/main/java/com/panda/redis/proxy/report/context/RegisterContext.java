package com.panda.redis.proxy.report.context;

import com.panda.redis.proxy.report.ProxyRegister;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegisterContext {

    private static Map<String, ProxyRegister> proxyRegisterMap = new ConcurrentHashMap<>(10);

    public static void addProxyRegister(String prefix, ProxyRegister proxyRegister){
        proxyRegisterMap.put(prefix, proxyRegister);
    }

    public static void getProxyRegister(String prefix, ProxyRegister proxyRegister){
        proxyRegisterMap.get(prefix);
    }

    public static ProxyRegister getProxyRegisterByRegisterAddress(String address){
        String prefix = address.substring(0, address.indexOf(":")+1);
        return proxyRegisterMap.get(prefix);
    }

}
