package com.panda.redis.core.address.zkImpl;

import com.panda.redis.base.common.LogUtil;
import com.panda.redis.base.constants.ProxyConstants;
import com.panda.redis.core.address.ProxyLoader;
import com.panda.redis.core.address.ProxyLoaderContext;
import com.panda.redis.core.loadBalance.ProxyLoadBalance;
import com.panda.redis.core.properties.GroupProxy;
import com.panda.redis.core.properties.PandaRedisProperties;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.nio.file.Path;
import java.util.*;

@Service
public class ZookeeperProxyRegisterImpl implements ProxyLoader, ApplicationContextAware {

    @Autowired
    private PandaRedisProperties pandaRedisProperties;

    private CuratorCrud curatorCrud;

    private static final String PREFIX = "zookeeper:";

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
        List<GroupProxy> groupProxyList = new ArrayList<>();
        Map<String, GroupProxy> groupProxyMap = new HashMap<>();
        groupList.stream().forEach(childPath ->{
            String groupPath = ProxyConstants.GROUP_REGISTER + "/" + childPath;
            GroupProxy groupProxy = groupProxyMap.get(groupPath);
            if(groupProxy == null){
                groupProxy = new GroupProxy();
                groupProxyMap.put(groupPath, groupProxy);
                ZookeeperContext.addToRegister(groupPath);
            }
            ProxyLoadBalance proxyLoadBalance = (ProxyLoadBalance) applicationContext.getBean("proxyLoadBalance");
            groupProxy.setProxyLoadBalanceRule(proxyLoadBalance);
            List<String> proxyList = curatorCrud.getChildren(groupPath);
            groupProxy.reflushClients(proxyList, groupPath);
            groupProxyList.add(groupProxy);

        });
        Assert.notEmpty(groupProxyList,"no proxy list");
        ProxyLoaderContext.groupProxyMap.putAll(groupProxyMap);
        ProxyLoaderContext.reflushProxyList();
    }



    /**
     * 注册大集群监听，监听集群变化
     */
    @Override
    public void registerProxiesListener() {
        try {
            curatorCrud.listen(ProxyConstants.GROUP_REGISTER, (treeCacheEvent)->{
                TreeCacheEvent.Type eventType = treeCacheEvent.getType();
                ChildData data = treeCacheEvent.getData();
                switch (eventType) {
                    case NODE_ADDED:
                        LogUtil.info("NODE_ADDED : " + data.getPath() + "  数据:" + new String(data.getData()));
                        addGroupOrProxies(data);
                        break;
                    case NODE_REMOVED:
                        LogUtil.info("NODE_REMOVED : " + data.getPath() + "  数据:" + new String(data.getData()));
                        removeGroupOrProxies(data);
                        break;
//                    case NODE_UPDATED:
//                        System.out.println("NODE_UPDATED : " + data.getPath() + "  数据:" + new String(data.getData()));
//                        break;
                    default:
                        break;
                }
            });
        } catch (Exception e) {
            LogUtil.error("syn group fail", e);
        }

    }

    /**
     * 移除proxy
     * @param data
     */
    private void removeGroupOrProxies(ChildData data) {
        String path = data.getPath();
        String group = path.substring(0, path.lastIndexOf("/"));
        GroupProxy groupProxy = ProxyLoaderContext.groupProxyMap.get(group);
        List<String> children = curatorCrud.getChildren(group);
        groupProxy.reflushClients(children, group);
    }

    /**
     * 添加
     * @param data
     */
    private void addGroupOrProxies(ChildData data){
        ProxyLoadBalance proxyLoadBalance = (ProxyLoadBalance) applicationContext.getBean("proxyLoadBalance");
        String path = data.getPath();
        String group = path.substring(0, path.lastIndexOf("/"));
        List<String> children = curatorCrud.getChildren(group);
        ProxyLoaderContext.addGroup(path, children, proxyLoadBalance);
        ProxyLoaderContext.reflushProxyList();
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
