package com.panda.redis.core.properties;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.context.ServersContext;
import com.panda.redis.core.loadBalance.ClientLoadBalance;
import com.panda.redis.core.loadBalance.GroupLoadBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class GroupClient {

    private ClientLoadBalance clientLoadBalanceRule;

    private String clientLoadBalance;

    private String id;

    private List<String> nodes;

    private List<Client> clients;

    public GroupClient(){
        // 每一个集群生成一个id
        id = StringUtils.isEmpty(id)? UUID.randomUUID().toString() : id;
        clients = new ArrayList<>();
        nodes.stream().map(Client::new).forEach(clients::add);
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

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public ClientLoadBalance getClientLoadBalanceRule() {
        return clientLoadBalanceRule;
    }

    public void setClientLoadBalanceRule(ClientLoadBalance clientLoadBalance) {
        this.clientLoadBalanceRule = clientLoadBalance;
    }

    public Client chooseClient(ServersContext serversContext) {
        return clientLoadBalanceRule.chooseClient(serversContext);
    }

    public String getClientLoadBalance() {
        return clientLoadBalance;
    }

    public void setClientLoadBalance(String clientLoadBalance) {
        this.clientLoadBalance = clientLoadBalance;
    }
}
