package com.panda.redis.core.address;

public interface ProxyLoader {

    default void initRegister(){

    }

    void loadAddress();

    void registerProxiesListener();
}
