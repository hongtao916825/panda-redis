package com.panda.redis.dash.dash.config;

import com.panda.redis.dash.dash.health.HealthReport;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ReporterConfig implements BeanDefinitionRegistryPostProcessor {

    private static final Map<String, String> HEALTH_REPORTER = new ConcurrentHashMap<>(100);

    static {
        HEALTH_REPORTER.put("zookeeper:", "com.panda.redis.dash.dash.health.zkimpl.ZookeeperHealthReport");
    }

    @SneakyThrows
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        ClassPathResource pathResource = new ClassPathResource("application.yml");
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(pathResource);
        Properties properties = yaml.getObject();
        String connectionString = properties.get("panda.redis.registerAddress").toString();
        String prefix = connectionString.substring(0, connectionString.indexOf(":") + 1);
        String connection = connectionString.replace(prefix, "");
        String beanClassName = HEALTH_REPORTER.get(prefix);
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(Class.forName(beanClassName));
        rootBeanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, connection);
        beanDefinitionRegistry.registerBeanDefinition("healthReport", rootBeanDefinition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
