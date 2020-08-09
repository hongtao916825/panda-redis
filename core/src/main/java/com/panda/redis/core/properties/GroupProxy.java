package com.panda.redis.core.properties;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.address.ProxyLoaderContext;
import com.panda.redis.core.context.ServersContext;
import com.panda.redis.core.loadBalance.ProxyLoadBalance;
import com.sun.xml.internal.ws.api.policy.PolicyResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class GroupProxy {

    private ProxyLoadBalance proxyLoadBalanceRule;

    private String id;

    private List<String> nodes;

    private List<Client> clients;

    public GroupProxy(){
        // 每一个集群生成一个id
        id = StringUtils.isEmpty(id)? UUID.randomUUID().toString() : id;
        clients = new ArrayList<>();
    }

    public GroupProxy(String id){
        // 每一个集群生成一个id
        clients = new ArrayList<>();
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

    public GroupProxy addClients(List<Client> clients) {
        this.clients.addAll(clients);
        return this;
    }

    public GroupProxy addClient(Client client) {
        this.clients.add(client);
        return this;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
        nodes.stream().map(Client::new).forEach(clients::add);
    }

    public ProxyLoadBalance getProxyLoadBalanceRule() {
        return proxyLoadBalanceRule;
    }

    public void setProxyLoadBalanceRule(ProxyLoadBalance clientLoadBalance) {
        this.proxyLoadBalanceRule = clientLoadBalance;
    }

    public Client chooseClient() {
        return proxyLoadBalanceRule.chooseProxy();
    }

    public void reflushClients(List<String> childPath,String path) {
        synchronized (this){
            this.getClients().clear();
            if(childPath.isEmpty()){
                // 没有proxy，剔除集群
                ProxyLoaderContext.removeGroup(path);
            }
            childPath.stream().map(Client :: new).forEach(this.getClients()::add);
        }
    }
}
