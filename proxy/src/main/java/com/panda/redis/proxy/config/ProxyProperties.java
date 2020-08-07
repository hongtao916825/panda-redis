package com.panda.redis.proxy.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Data
public class ProxyProperties {

    private List<String> nodes;

    private String password;

    private Integer database;

    private Integer bindPort;



}
