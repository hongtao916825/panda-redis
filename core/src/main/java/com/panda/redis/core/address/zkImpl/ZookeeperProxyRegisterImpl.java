package com.panda.redis.core.address.zkImpl;

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
        ProxyLoadBalance proxyLoadBalance = (ProxyLoadBalance) applicationContext.getBean("proxyLoadBalance");
        Assert.notEmpty(groupList,"no group list");
        List<GroupProxy> groupProxyList = new ArrayList<>();
        Map<String, GroupProxy> groupProxyMap = new HashMap<>();
        groupList.stream().forEach(childPath ->{
            String groupPath = ProxyConstants.GROUP_REGISTER + "/" + childPath;
            GroupProxy groupProxy = groupProxyMap.get(groupPath);
            if(groupProxy == null){
                groupProxy = new GroupProxy();
                groupProxyMap.put(groupPath, groupProxy);
                this.registerProxiesListener(groupPath);
            }
            groupProxy.setProxyLoadBalanceRule(proxyLoadBalance);
            List<String> proxyList = curatorCrud.getChildren(groupPath);
            groupProxy.reflushClients(proxyList, groupPath);
            groupProxyList.add(groupProxy);

        });
        Assert.notEmpty(groupProxyList,"no proxy list");
        ProxyLoaderContext.groupProxyMap.putAll(groupProxyMap);
        ProxyLoaderContext.reflushProxyList();
        this.registerGroupListener(proxyLoadBalance);
    }

    private void registerGroupListener(ProxyLoadBalance proxyLoadBalance) {
        try {
            curatorCrud.lister(ProxyConstants.GROUP_REGISTER, (changedParams)->{
                List<String> childPath = changedParams.getChildPath();
                ProxyLoaderContext.addGroup(changedParams.getParentPath(), childPath, proxyLoadBalance);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void registerProxiesListener(String path) {
        try {
            curatorCrud.lister(path, (changedParams)->{
                String parentPath = changedParams.getParentPath();
                List<String> childPath = changedParams.getChildPath();
                GroupProxy groupProxy = ProxyLoaderContext.groupProxyMap.get(parentPath);
                groupProxy.reflushClients(childPath, parentPath);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
