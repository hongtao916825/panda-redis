package com.panda.redis.proxy.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class ProxyProperties {

    @Value("${redis.cluster:single}")
    private String cluster;

    @Value("#{'${redis.nodes}'.split(',')}")
    private List<String> nodes;

    @Value("${redis.password:single:null}")
    private String password;

    @Value("${redis.database:0}")
    private Integer database;

    @Value("${server.bindPort}")
    private Integer bindPort;

    @Value("${server.enable.redundant:false}")
    private Boolean enableRedundant;

    @Value("${server.workThread:0}")
    private Integer workThread;

}
