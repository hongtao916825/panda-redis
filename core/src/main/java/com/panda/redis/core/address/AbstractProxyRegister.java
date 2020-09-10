package com.panda.redis.core.address;

import com.google.common.base.Strings;
import com.panda.redis.core.pojo.GroupProxy;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractProxyRegister implements ProxyLoader{

    @Override
    public void loadAddress(){
        Map<String, GroupProxy> groupProxyMap = new HashMap<>();
        List<GroupProxy> groupProxies = doLoadAddress();
        groupProxies.stream().forEach(groupProxy -> {
            groupProxy.refreshProxies();
            Assert.isTrue(!Strings.isNullOrEmpty(groupProxy.getId()), "group id can not be null or empty string");
            groupProxyMap.put(groupProxy.getId(), groupProxy);
        });
        Assert.notEmpty(groupProxyMap,"no proxy list");
        ProxyLoaderContext.groupProxyMap.putAll(groupProxyMap);
        ProxyLoaderContext.refreshGroupList();
    }

    protected abstract List<GroupProxy> doLoadAddress();

}
