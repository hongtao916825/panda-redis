package com.panda.redis.core.autoconfigure;

import com.panda.redis.base.common.LogUtil;
import com.panda.redis.core.address.LoadAddressListener;
import com.panda.redis.core.address.ProxyLoaderContext;
import com.panda.redis.core.properties.PandaRedisProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
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
@Import({PandaConfiguration.class, LoadAddressListener.class})
public class PandaRedisAutoConfiguration implements BeanDefinitionRegistryPostProcessor,EnvironmentAware{

    private static final String DEFAULT_GROUP_LOADBALANCE = "com.panda.redis.core.loadBalance.impl.KeyHashLoadBalance";
    private static final String DEFAULT_REGISTER_BEAN = "com.panda.redis.core.address.zkImpl.ZookeeperProxyRegisterImpl";
    private static final String DEFAULT_PROXY_LOADBALANCE = "com.panda.redis.core.loadBalance.impl.RoundRobinProxyLoadBalance";

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        try {
            String className = environment.getProperty("panda.redis.groupLoadBalance");
            if(StringUtils.isEmpty(className)){
                className = DEFAULT_GROUP_LOADBALANCE;
            }
            RootBeanDefinition beanDefinition = new RootBeanDefinition(Class.forName(className));
            beanDefinitionRegistry.registerBeanDefinition("groupLoadBalance", beanDefinition);

            String registerAddress = environment.getProperty("panda.redis.registerAddress");
            String registerBeanName = ProxyLoaderContext.getProxyRegisterByRegisterAddress(registerAddress);
            if(StringUtils.isEmpty(registerBeanName)){
                registerBeanName = DEFAULT_REGISTER_BEAN;
            }
            beanDefinitionRegistry.registerBeanDefinition("proxyLoader", new RootBeanDefinition(Class.forName(registerBeanName)));

            String proxyLoadBalance = environment.getProperty("panda.redis.proxyLoadBalance");
            String proxyLoadBalanceBeanName = ProxyLoaderContext.getProxyRegisterByRegisterAddress(registerAddress);
            if(StringUtils.isEmpty(proxyLoadBalance)){
                proxyLoadBalanceBeanName = DEFAULT_PROXY_LOADBALANCE;
            }
            RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(Class.forName(proxyLoadBalanceBeanName));
            rootBeanDefinition.setLazyInit(true);
            rootBeanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
            beanDefinitionRegistry.registerBeanDefinition("proxyLoadBalance", rootBeanDefinition);
        } catch (ClassNotFoundException e) {
            LogUtil.error("load bean fail: " + e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }


}
