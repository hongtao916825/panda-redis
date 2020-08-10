package com.panda.redis.proxy.config;

import com.panda.redis.proxy.report.ProxyRegister;
import health.HealthReporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ProxyRegisterListener implements ApplicationListener<ContextRefreshedEvent> {

    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10, new NamedThreadFactory("health-report"));

    @Autowired
    private ProxyRegister proxyRegister;

    @Autowired
    private HealthReporter healthReporter;

    @Autowired
    private ProxyProperties proxyProperties;

    private void startThreadHealth() {
        scheduledExecutorService.scheduleWithFixedDelay(healthReporter, 0, 10, TimeUnit.SECONDS);
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
