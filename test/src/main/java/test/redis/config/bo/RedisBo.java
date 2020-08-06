package test.redis.config.bo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.DefaultLettucePool;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePool;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.IOException;

@Slf4j
public class RedisBo {

    public RedisTemplate redisTemplate;

    public StringRedisTemplate stringRedisTemplate;

    public RedisBo() {
        postProcessBeanDefinitionRegistry();
    }

    public void postProcessBeanDefinitionRegistry() {
        try {
            LettuceConnectionFactory lettuceConnectionFactory = createConnectionFactory();
            // 创建redisTemplate
            redisTemplate = new RedisTemplate();
            redisTemplate.setConnectionFactory(lettuceConnectionFactory);
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
            redisTemplate.setHashKeySerializer(new StringRedisSerializer());
            redisTemplate.afterPropertiesSet();
            // 创建StringRedisTemplate
            stringRedisTemplate = new StringRedisTemplate();
            stringRedisTemplate.setConnectionFactory(lettuceConnectionFactory);
            stringRedisTemplate.afterPropertiesSet();
        } catch (IOException e) {
            log.error("创建redistemplate异常", e);
            throw new RuntimeException("创建redistemplate异常", e);
        }
    }

    private LettuceConnectionFactory createConnectionFactory()  throws IOException {
        //new一个基于Resource的PropertySource
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(createPoolConfig());
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }


    private LettucePool createPoolConfig(){
        DefaultLettucePool lettucePool = new DefaultLettucePool();
//        lettucePool.setHostName("47.97.215.217");
        lettucePool.setHostName("127.0.0.1");
        lettucePool.setPort(6061);
        lettucePool.afterPropertiesSet();
        return lettucePool;
    }

}
