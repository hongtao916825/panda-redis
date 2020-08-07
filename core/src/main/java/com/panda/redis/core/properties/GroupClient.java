package com.panda.redis.core.properties;

import com.panda.redis.base.api.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GroupClient {

    private String id;

    private List<Client> clients;

    public GroupClient(){
        // 每一个集群生成一个id
        this.clients = new ArrayList<>();
        // 每一个集群生成一个id
        this.id = UUID.randomUUID().toString();
    }

    public GroupClient(String id){
        this.clients = new ArrayList<>();
        // 每一个集群生成一个id
        this.id = id;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GroupClient addClients(List<Client> clients) {
        this.clients.addAll(clients);
        return this;
    }

    public GroupClient addClient(Client client) {
        this.clients.add(client);
        return this;
    }

}
