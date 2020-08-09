package com.panda.redis.core.address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class LoadAddressListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ProxyLoader proxyLoader;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        proxyLoader.initRegister();
        proxyLoader.loadAddress();
    }
}
