package com.panda.redis.core.address.zkImpl;

import com.panda.redis.base.api.Client;
import com.panda.redis.base.constants.ProxyConstants;
import com.panda.redis.core.address.ProxyLoader;
import com.panda.redis.core.address.ProxyLoaderContext;
import com.panda.redis.core.context.PandaJedisPool;
import com.panda.redis.core.loadBalance.ProxyLoadBalance;
import com.panda.redis.core.properties.GroupProxy;
import com.panda.redis.core.properties.PandaRedisProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ZookeeperProxyRegisterImpl implements ProxyLoader, ApplicationContextAware {

    @Autowired
    private PandaRedisProperties pandaRedisProperties;

    private CuratorCrud curatorCrud;

    private static final String PREFIX = "zookeeper:";

    @Autowired
    private PandaJedisPool pandaJedisPool;
    @Override
    public void initRegister(){
        String registerAddress = pandaRedisProperties.getRegisterAddress();
        String connectString = registerAddress.replace(PREFIX, "");
        curatorCrud = new CuratorCrud(connectString);
    }

    @Override
    public void loadAddress() {
        List<String> groupList = curatorCrud.getChildren(ProxyConstants.GROUP_REGISTER);
        Assert.notEmpty(groupList,"no group list");
        System.out.println(groupList);
        List<GroupProxy> groupProxyList = new ArrayList<>();
        Map<String, GroupProxy> groupProxyMap = new HashMap<>();
        groupList.stream().forEach(groupPath ->{
            GroupProxy groupProxy = groupProxyMap.get(groupPath);
            if(groupProxy == null){
                groupProxy = new GroupProxy();
                groupProxyMap.put(groupPath, groupProxy);
            }
            List<String> proxyList = curatorCrud.getChildren(ProxyConstants.GROUP_REGISTER+"/"+groupPath);
            Assert.notEmpty(proxyList,"no proxy list");
            groupProxy.setProxyLoadBalanceRule((ProxyLoadBalance) applicationContext.getBean("proxyLoadBalance"));
            List<Client> clientList = proxyList.stream().map(proxy -> proxy.replace("/", "")).map(Client::new).collect(Collectors.toList());
            groupProxy.addClients(clientList);
            groupProxyList.add(groupProxy);
        });
        ProxyLoaderContext.groupProxyList.addAll(groupProxyList);
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
