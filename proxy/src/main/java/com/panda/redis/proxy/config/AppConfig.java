package com.panda.redis.proxy.config;

import com.panda.redis.proxy.base.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Enumeration;
import java.util.Properties;

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


}
