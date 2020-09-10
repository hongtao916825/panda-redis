package com.panda.redis.proxy.config;

import com.panda.redis.base.common.LogUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProxyRegisterCreation implements BeanDefinitionRegistryPostProcessor {

    // 服务注册
    private static final Map<String, String> PROXY_REGISTER_MAP = new ConcurrentHashMap<>(10);
    // 健康检查
    private static final Map<String, String> PROXY_HEALTH_MAP = new ConcurrentHashMap<>(10);

    static {
        PROXY_REGISTER_MAP.put("zookeeper:", "com.panda.redis.proxy.report.zkImpl.ZookeeperProxyRegisterImpl");
        PROXY_HEALTH_MAP.put("zookeeper:", "com.panda.redis.proxy.health.zkImpl.ZookeeperHealthReport");
    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        try {
            ClassPathResource pathResource = new ClassPathResource("application.yml");
            YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
            yaml.setResources(pathResource);
            Properties properties = yaml.getObject();
            Assert.notNull(properties, "application.yml can not be found");
            String address = properties.getProperty("server.registerAddress");
            Assert.notNull(address,"server.registerAddress can not be found");
            String prefix = address.substring(0, address.indexOf(":")+1);
            String registerBeanName = PROXY_REGISTER_MAP.get(prefix);
            Assert.notNull(registerBeanName, "no register bean");
            RootBeanDefinition beanDefinition = new RootBeanDefinition(Class.forName(registerBeanName));
            beanDefinitionRegistry.registerBeanDefinition("proxyRegister",beanDefinition);
            boolean healthReport = Boolean.parseBoolean(properties.getProperty("server.enable.com.panda.redis.proxy.health-report"));
            if(healthReport) {
                String healthBeanName = PROXY_HEALTH_MAP.get(prefix);
                Assert.notNull(healthBeanName, "no register bean");
                RootBeanDefinition healthBeanDefinition = new RootBeanDefinition(Class.forName(healthBeanName));
                beanDefinitionRegistry.registerBeanDefinition("healthReporter", healthBeanDefinition);
            }
        } catch (ClassNotFoundException e) {
            LogUtil.error("init register fail", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

}
