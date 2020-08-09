package com.panda.redis.proxy.report;

import org.springframework.stereotype.Service;

public interface ProxyRegister {

    default void initRegister(){

    }

    void register();


}
