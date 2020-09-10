package com.panda.redis.core.address.zkImpl;

import com.panda.redis.base.common.LogUtil;
import com.panda.redis.base.constants.ProxyConstants;
import com.panda.redis.core.address.AbstractProxyRegister;
import com.panda.redis.core.address.ProxyLoader;
import com.panda.redis.core.address.ProxyLoaderContext;
import com.panda.redis.core.context.ServersContext;
import com.panda.redis.core.loadBalance.ProxyLoadBalance;
import com.panda.redis.core.pojo.GroupProxy;
import com.panda.redis.core.properties.PandaRedisProperties;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ZookeeperProxyRegisterImpl extends AbstractProxyRegister implements ApplicationContextAware {

    @Autowired
    private JedisPoolConfig jedisPoolConfig;

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
    public List<GroupProxy> doLoadAddress() {
        List<String> groupList = curatorCrud.getChildren(ProxyConstants.GROUP_REGISTER);
        Assert.notEmpty(groupList,"no group list");
        Map<String, GroupProxy> groupProxyMap = new HashMap<>();
        return groupList.stream().map(childPath -> {
            String groupId = childPath;
            String groupPath = ProxyConstants.GROUP_REGISTER + "/" + childPath;
            ProxyLoadBalance proxyLoadBalance = (ProxyLoadBalance) applicationContext.getBean("proxyLoadBalance");
            Set<String> proxyList = new HashSet<>(curatorCrud.getChildren(groupPath));
            GroupProxy groupProxy = groupProxyMap.get(groupPath);
            if (groupProxy == null) {
                groupProxy = new GroupProxy(groupId, jedisPoolConfig, proxyList, proxyLoadBalance);
                groupProxyMap.put(groupId, groupProxy);
            }
            return groupProxy;
        }).collect(Collectors.toList());
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
                if(data != null && !data.getPath().equals(ProxyConstants.GROUP_REGISTER)) {
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
                    ProxyLoaderContext.refreshGroupList();
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
        String[] pathArr = path.split("/");
        if(pathArr.length == 4){
            removeProxies(data);
        }else{
            removeGroup(data);
        }
    }

    /**
     * 移除集群，因为集群是永久节点，所以该方法不会主动触发
     * @param data
     */
    private void removeGroup(ChildData data) {
        String groupPath = data.getPath();
        String groupId = groupPath.substring(groupPath.lastIndexOf("/")+1);
        ProxyLoaderContext.removeGroup(groupId);
    }


    /**
     * 移除代理
     * @param data
     */
    private void removeProxies(ChildData data) {
        String path = data.getPath();
        String[] pathArr = path.split("/");
        String groupId = pathArr[2];
        String proxyPath = pathArr[3];
        GroupProxy groupProxy = ProxyLoaderContext.groupProxyMap.get(groupId);
        // 没有这个集群就不需要别的操作
        if(groupProxy != null){
            groupProxy.removeProxies(proxyPath);
        }

    }

    /**
     * 添加
     * @param data
     */
    private void addGroupOrProxies(ChildData data){
        String path = data.getPath();
        String[] pathArr = path.split("/");
        if(pathArr.length == 4){
            addProxies(data);
        }else{
            addGroup(data);
        }
    }

    /**
     * 添加集群
     * @param data
     */
    private void addGroup(ChildData data) {
//         /panda.proxy.group/1
        ProxyLoadBalance proxyLoadBalance = (ProxyLoadBalance) applicationContext.getBean("proxyLoadBalance");
        String groupPath = data.getPath();
        String groupId = groupPath.substring(groupPath.lastIndexOf("/")+1);
        Set<String> proxyList = new HashSet<>(curatorCrud.getChildren(groupPath));
        GroupProxy groupProxy = ProxyLoaderContext.groupProxyMap.get(groupId);
        if(groupProxy == null){
            // 集群未添加，先添加集群，因为没有这个集群所有就做全量更新
            groupProxy = new GroupProxy(groupId, jedisPoolConfig, proxyList, proxyLoadBalance);
            ProxyLoaderContext.groupProxyMap.put(groupId, groupProxy);
            groupProxy.refreshProxies();
        }else {
            proxyList.forEach(groupProxy::addProxy);
        }
    }

    /**
     * 添加代理
     * @param data
     */
    private void addProxies(ChildData data) {
        ProxyLoadBalance proxyLoadBalance = (ProxyLoadBalance) applicationContext.getBean("proxyLoadBalance");
        String path = data.getPath();
        String[] pathArr = path.split("/");
        String groupId = pathArr[2];
        String proxyPath = pathArr[3];
        GroupProxy groupProxy = ProxyLoaderContext.groupProxyMap.get(groupId);
        if(groupProxy == null){
            // 集群未添加，先添加集群，因为没有这个集群所有就做全量更新
            groupProxy = new GroupProxy(groupId, jedisPoolConfig, proxyLoadBalance);
            ProxyLoaderContext.groupProxyMap.put(groupId, groupProxy);
        }
        groupProxy.addProxy(proxyPath);
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
