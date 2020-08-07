package com.panda.redis.core.properties;

import lombok.Data;

import java.util.List;

public class PandaRedisProperties {

    private List<GroupClient> groupClients;

    public List<GroupClient> getGroupClients() {
        return groupClients;
    }

    public void setGroupClients(List<GroupClient> groupClients) {
        this.groupClients = groupClients;
    }
}
