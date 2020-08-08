package com.panda.redis.core.autoconfigure;

import com.panda.redis.core.context.PandaJedisPool;
import com.panda.redis.core.context.RedisLoadBalance;
import com.panda.redis.core.loadBalance.ClientLoadBalance;
import com.panda.redis.core.loadBalance.GroupLoadBalance;
import com.panda.redis.core.loadBalance.impl.KeyHashLoadBalance;
import com.panda.redis.core.properties.PandaRedisProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;

@Slf4j
public class PandaConfiguration implements ApplicationContextAware {

    private static final String DEFAULT_CLIENT_LOADBALANCE = "com.panda.redis.core.loadBalance.impl.RoundRobinClientLoadBalance";




    @Autowired
    private PandaRedisProperties pandaRedisProperties;

    @Bean
//    @ConditionalOnProperty(prefix = PandaRedisProperties.PANDAREDIS_PREFIX,name = "USEHA",havingValue ="false")
    public JedisPool createJedisPool() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        log.info("加载jedis连接池");
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(pandaRedisProperties.getMaxTotal());
        jedisPoolConfig.setMaxIdle(pandaRedisProperties.getMaxIdel());
        jedisPoolConfig.setMinIdle(pandaRedisProperties.getMinIdel());
        jedisPoolConfig.setTestOnBorrow(pandaRedisProperties.isTestOnBorrow());
        jedisPoolConfig.setTestOnReturn(pandaRedisProperties.isTestOnRetrun());
        PandaJedisPool jedisPool = new PandaJedisPool(jedisPoolConfig, pandaRedisProperties.getGroupClients());
        return jedisPool;
    }

    @Bean
    @ConditionalOnBean(value = JedisPool.class)
    public RedisLoadBalance createRedisLoadBalance(JedisPool jedisPool){
        RedisLoadBalance loadBalance = new RedisLoadBalance(jedisPool);
        return loadBalance;
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void setClientLoadbalance() throws Exception {
        pandaRedisProperties.getGroupClients().forEach(k ->{
            String clientLoadBalanceName = k.getClientLoadBalance();
            if(StringUtils.isEmpty(clientLoadBalanceName)){
                clientLoadBalanceName = DEFAULT_CLIENT_LOADBALANCE;
            }
            try {
                Class clazz = Class.forName(clientLoadBalanceName);
                ClientLoadBalance clientLoadBalance = (ClientLoadBalance)clazz.newInstance();
                applicationContext.getAutowireCapableBeanFactory().autowireBean(clientLoadBalance);
                k.setClientLoadBalanceRule(clientLoadBalance);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

        });

    }



}
