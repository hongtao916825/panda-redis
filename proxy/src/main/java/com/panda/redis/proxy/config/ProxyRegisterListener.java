package com.panda.redis.proxy.config;

import com.panda.redis.proxy.report.ProxyRegister;
import com.panda.redis.proxy.health.HealthReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class ProxyRegisterListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ProxyRegister proxyRegister;

    @Autowired
    private HealthReporter healthReporter;

    @Autowired
    private ProxyProperties proxyProperties;

    private void startThreadHealth() {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10, new NamedThreadFactory("com.panda.redis.proxy.health-report"));
        scheduledExecutorService.scheduleWithFixedDelay(healthReporter, 0, proxyProperties.getDelay(), TimeUnit.SECONDS);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//        ProxyRegister proxyRegister = RegisterContext.getProxyRegisterByRegisterAddress(proxyProperties.getRegisterAddress());
        proxyRegister.initRegister();
        proxyRegister.register();
        if(proxyProperties.getHealthReport()){
            startThreadHealth();
        }
    }

}
