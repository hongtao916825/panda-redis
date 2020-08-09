package com.panda.redis.proxy.report.context;

import com.panda.redis.proxy.config.ProxyProperties;
import com.panda.redis.proxy.report.ProxyRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class ProxyRegisterCreation implements ApplicationListener {

    @Autowired
    private ProxyProperties proxyProperties;

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        ProxyRegister proxyRegister = RegisterContext.getProxyRegisterByRegisterAddress(proxyProperties.getRegisterAddress());
        proxyRegister.initRegister();
        proxyRegister.register();
    }
}
