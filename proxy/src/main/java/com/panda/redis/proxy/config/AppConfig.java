package com.panda.redis.proxy.config;

import com.panda.redis.proxy.base.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Enumeration;
import java.util.Properties;

@ComponentScan(basePackages = {
        "com.panda.redis"
})
@Configuration
@Import(NettyServer.class)
@Slf4j
public class AppConfig {

    @Bean
    public PropertySourcesPlaceholderConfigurer createPropertiesPropertySourceConfigurer(@Autowired Environment env) throws Exception {
        ClassPathResource pathResource = new ClassPathResource("application.yml");
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(pathResource);
        Properties properties = yaml.getObject();
        configurer.setProperties(properties);
        configurer.setLocalOverride(true);
        return configurer;
    }


    @Bean
    public JedisPool createJedisPool(PandaRedisProperties pandaRedisProperties, ProxyProperties proxyProperties) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        log.info("加载jedis连接池");
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(100);
        jedisPoolConfig.setMaxIdle(pandaRedisProperties.getMaxIdel());
        jedisPoolConfig.setMinIdle(pandaRedisProperties.getMinIdel());
        jedisPoolConfig.setTestOnBorrow(pandaRedisProperties.isTestOnBorrow());
        jedisPoolConfig.setTestOnReturn(pandaRedisProperties.isTestOnRetrun());
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, proxyProperties.getNodes().get(0).split(":")[0],Integer.valueOf(proxyProperties.getNodes().get(0).split(":")[1]));
        return jedisPool;
    }


}
