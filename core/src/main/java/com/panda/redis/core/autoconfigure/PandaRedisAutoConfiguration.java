package com.panda.redis.core.autoconfigure;

import com.panda.redis.core.properties.PandaRedisProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableConfigurationProperties(PandaRedisProperties.class)
@ConditionalOnClass(value = {
        Jedis.class, JedisPool.class, JedisPoolConfig.class
})
@Slf4j
@Import(PandaConfiguration.class)
public class PandaRedisAutoConfiguration implements BeanDefinitionRegistryPostProcessor,EnvironmentAware{

    private static final String DEFAULT_GROUP_LOADBALANCE = "com.panda.redis.core.loadBalance.impl.KeyHashLoadBalance";

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @SneakyThrows
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        String className = environment.getProperty("panda.redis.groupLoadBalance");
        if(StringUtils.isEmpty(className)){
            className = DEFAULT_GROUP_LOADBALANCE;
        }
        RootBeanDefinition beanDefinition = new RootBeanDefinition(Class.forName(className));
        String alias = "groupLoadBalance";
        beanDefinitionRegistry.registerBeanDefinition(alias, beanDefinition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }


}
