package com.panda.redis.core.autoconfigure;

import com.panda.redis.core.context.RedisLoadBalance;
import com.panda.redis.core.context.PandaJedisPool;
import com.panda.redis.core.loadBalance.ClientLoadBalance;
import com.panda.redis.core.loadBalance.GroupLoadBalance;
import com.panda.redis.core.loadBalance.impl.KeyHashLoadBalance;
import com.panda.redis.core.properties.GroupClient;
import com.panda.redis.core.properties.PandaRedisProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.xml.bind.annotation.XmlType;

@Configuration
@EnableConfigurationProperties(PandaRedisProperties.class)
@ConditionalOnClass(value = {
        Jedis.class, JedisPool.class, JedisPoolConfig.class
})
@Slf4j
public class PandaRedisAutoConfiguration implements ApplicationContextAware, InitializingBean {

    private static final String DEFAULT_CLIENT_LOADBALANCE = "com.panda.redis.core.loadBalance.impl.RoundRobinClientLoadBalance";
    private static final String DEFAULT_GROUP_LOADBALANCE = "com.panda.redis.core.loadBalance.impl.KeyHashLoadBalance";

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
        String groupLoadBalanceName = pandaRedisProperties.getGroupLoadBalance();
        if(StringUtils.isEmpty(groupLoadBalanceName)){
            groupLoadBalanceName = DEFAULT_GROUP_LOADBALANCE;
        }
        Class<?> groupLoadBalanceClass = Class.forName(groupLoadBalanceName);
        GroupLoadBalance groupLoadBalance = (GroupLoadBalance)groupLoadBalanceClass.newInstance();
        jedisPool.setGroupLoadBalance(groupLoadBalance);
        return jedisPool;
    }

    @Bean("groupLoadBalance")
    public GroupLoadBalance createGroupLoadBalance(){
        return new KeyHashLoadBalance();
    }

    @Bean
    @ConditionalOnBean(value = JedisPool.class)
    public RedisLoadBalance tulingRedis(JedisPool jedisPool){
        RedisLoadBalance loadBalance = new RedisLoadBalance(jedisPool);
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
//            ClientLoadBalance clientLoadBalance = (ClientLoadBalance) applicationContext.getBean(k.getClientLoadBalance());
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
