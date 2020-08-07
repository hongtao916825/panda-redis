package com.panda.redis.core.autoconfigure;

import com.panda.redis.core.context.LoadBalance;
import com.panda.redis.core.context.PandaJedisPool;
import com.panda.redis.core.loadBalance.ClientLoadBalance;
import com.panda.redis.core.loadBalance.GroupLoadBalance;
import com.panda.redis.core.loadBalance.impl.KeyHashLoadBalance;
import com.panda.redis.core.properties.PandaRedisProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.util.Pool;

@Configuration
@EnableConfigurationProperties(PandaRedisProperties.class)
@ConditionalOnClass(value = {
        Jedis.class, JedisPool.class, JedisPoolConfig.class
})
@Lazy(false)
@Slf4j
public class PandaRedisAutoConfiguration implements ApplicationContextAware, InitializingBean {

    @Autowired
    private PandaRedisProperties pandaRedisProperties;

    @Bean
    @ConditionalOnProperty(prefix = PandaRedisProperties.PANDAREDIS_PREFIX)
    public JedisPool createJedisPool(){
        log.info("加载jedis连接池");
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(pandaRedisProperties.getMaxTotal());
        jedisPoolConfig.setMaxIdle(pandaRedisProperties.getMaxIdel());
        jedisPoolConfig.setMinIdle(pandaRedisProperties.getMinIdel());
        jedisPoolConfig.setTestOnBorrow(pandaRedisProperties.isTestOnBorrow());
        jedisPoolConfig.setTestOnReturn(pandaRedisProperties.isTestOnRetrun());
        JedisPool jedisPool = new PandaJedisPool(jedisPoolConfig);
        return jedisPool;
    }

    @Bean("groupLoadBalance")
    public GroupLoadBalance createGroupLoadBalance(){
        return new KeyHashLoadBalance();
    }

    @Bean
    @ConditionalOnBean(value = JedisPool.class)
    public LoadBalance tulingRedis(JedisPool jedisPool){
        LoadBalance loadBalance = new LoadBalance(jedisPool);
        return loadBalance;
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pandaRedisProperties.getGroupClients().forEach(k ->{
            ClientLoadBalance clientLoadBalance = (ClientLoadBalance) applicationContext.getBean(k.getClientLoadBalance());
            k.setClientLoadBalanceRule(clientLoadBalance.cloneClientLoadBalance());
        });

    }
}
