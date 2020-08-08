package com.panda.redis.core.autoconfigure;

import com.panda.redis.core.context.RedisLoadBalance;
import com.panda.redis.core.context.PandaJedisPool;
import com.panda.redis.core.loadBalance.ClientLoadBalance;
import com.panda.redis.core.loadBalance.GroupLoadBalance;
import com.panda.redis.core.loadBalance.impl.KeyHashLoadBalance;
import com.panda.redis.core.properties.GroupClient;
import com.panda.redis.core.properties.PandaRedisProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import javax.xml.bind.annotation.XmlType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
