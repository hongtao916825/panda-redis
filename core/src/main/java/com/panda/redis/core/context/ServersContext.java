package com.panda.redis.core.context;

import com.panda.redis.base.api.Client;

import java.util.List;

public class ServersContext {

    private List<Client> servers;

    private String key;

    private String value;

    public ServersContext() {
    }

    public ServersContext(List<Client> servers) {
        this.servers = servers;
    }

    public ServersContext(List<Client> servers, String key, String value) {
        this.servers = servers;
        this.key = key;
        this.value = value;
    }

    public List<Client> getServers() {
        return servers;
    }

    public void setServers(List<Client> servers) {
        this.servers = servers;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
