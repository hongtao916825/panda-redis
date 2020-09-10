package com.panda.redis.proxy.config;

import com.panda.redis.base.common.LogUtil;
import com.panda.redis.base.constants.ProxyConstants;
import com.panda.redis.proxy.base.NettyServer;
import io.netty.util.internal.StringUtil;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import redis.clients.jedis.*;

import java.util.*;
import java.util.stream.Collectors;

@ComponentScan(basePackages = {
        "com.panda.redis"
})
@Configuration
@Import(NettyServer.class)
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
    public ProxyPool createJedisPool(PandaRedisProperties pandaRedisProperties, ProxyProperties proxyProperties) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        LogUtil.info("加载jedis连接池");
        ProxyPool proxyPool = new ProxyPool(proxyProperties.getCluster().toUpperCase());
        switch (proxyProperties.getCluster().toUpperCase()){
            case ProxyConstants.CLUSTER_SINGLE:
                proxyPool.setJedisPool(createSingleJedisPool(pandaRedisProperties, proxyProperties));
                break;
            case ProxyConstants.CLUSTER_SENTINEL:
                proxyPool.setJedisSentinelPool(createSentinelJedisPool(pandaRedisProperties, proxyProperties));
                break;
            case ProxyConstants.CLUSTER_CLUSTERE:
                proxyPool.setJedisCluster(createClusterJedisPool(pandaRedisProperties, proxyProperties));
                break;
            default:
                throw new RuntimeException("config redis.cluster wrong，require : SINGLE,SENTINEL,CLUSTER");
        }
        return proxyPool;
    }

    /**
     * cluster 集群
     * @param pandaRedisProperties
     * @param proxyProperties
     * @return
     */
    private JedisCluster createClusterJedisPool(PandaRedisProperties pandaRedisProperties, ProxyProperties proxyProperties) {
        JedisPoolConfig jedisPoolConfig = createJedisPoolConfig(pandaRedisProperties);
        Set<HostAndPort> nodes = proxyProperties.getNodes().stream().distinct()
                .map(node -> {
                    String[] address = node.split(ProxyConstants.NODE_PREFIX);
                    return new HostAndPort(address[0], Integer.valueOf(address[1]));
                }).collect(Collectors.toSet());
        JedisCluster jedisCluster = new JedisCluster(nodes, 2000, 2000, 5, proxyProperties.getPassword(), jedisPoolConfig);
        return jedisCluster;
    }

    /**
     * sentinel 集群
     * @param pandaRedisProperties
     * @param proxyProperties
     * @return
     */
    private JedisSentinelPool createSentinelJedisPool(PandaRedisProperties pandaRedisProperties, ProxyProperties proxyProperties) {
        JedisPoolConfig jedisPoolConfig = createJedisPoolConfig(pandaRedisProperties);
        String password = proxyProperties.getPassword();
        String masterName = proxyProperties.getMasterName();
        Set<String> nodes = new HashSet<>(proxyProperties.getNodes());
        return new JedisSentinelPool(masterName, nodes, jedisPoolConfig, password);

    }

    /**
     * sentinel 单节点
     * @param pandaRedisProperties
     * @param proxyProperties
     * @return
     */
    private JedisPool createSingleJedisPool(PandaRedisProperties pandaRedisProperties, ProxyProperties proxyProperties) {
        JedisPoolConfig jedisPoolConfig = createJedisPoolConfig(pandaRedisProperties);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, proxyProperties.getNodes().get(0).split(":")[0],Integer.valueOf(proxyProperties.getNodes().get(0).split(":")[1]));
        return jedisPool;
    }

    private JedisPoolConfig createJedisPoolConfig(PandaRedisProperties pandaRedisProperties) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(100);
        jedisPoolConfig.setMaxIdle(pandaRedisProperties.getMaxIdel());
        jedisPoolConfig.setMinIdle(pandaRedisProperties.getMinIdel());
        jedisPoolConfig.setTestOnBorrow(pandaRedisProperties.isTestOnBorrow());
        jedisPoolConfig.setTestOnReturn(pandaRedisProperties.isTestOnRetrun());
        return jedisPoolConfig;
    }


}
