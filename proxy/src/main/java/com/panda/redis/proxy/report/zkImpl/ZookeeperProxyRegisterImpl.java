package com.panda.redis.proxy.report.zkImpl;

import com.panda.redis.base.constants.ProxyConstants;
import com.panda.redis.proxy.config.ProxyProperties;
import com.panda.redis.proxy.report.ProxyRegister;
import com.panda.redis.proxy.report.context.RegisterContext;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class ZookeeperProxyRegisterImpl implements ProxyRegister {

    @Autowired
    private ProxyProperties proxyProperties;

    private CuratorCrud curatorCrud;

    private static final String PREFIX = "zookeeper:";

    @PostConstruct
    public void addRegister(){
        RegisterContext.addProxyRegister(PREFIX, this);

    }

    @Override
    public void initRegister(){
//        String registerAddress = proxyProperties.getRegisterAddress();
//        String connectString = registerAddress.replace("zookeeper:", "");
//        try {
//            zookeeper=new ZooKeeper(connectString,5000,null);
//        } catch (IOException e) {
//            throw new RuntimeException("init fail", e);
//        }
        String registerAddress = proxyProperties.getRegisterAddress();
        String connectString = registerAddress.replace(PREFIX, "");
        curatorCrud = new CuratorCrud(connectString);
    }

    @Override
    public void register() {
        try {
            Boolean exists = curatorCrud.exists(ProxyConstants.GROUP_REGISTER);
            if(exists){
//                curatorCrud.setData(ProxyConstants.GROUP_REGISTER + "/" + proxyProperties.getGroupId(), InetAddress.getLocalHost().getHostAddress()+":" + proxyProperties.getBindPort());
                if(!curatorCrud.exists(ProxyConstants.GROUP_REGISTER + "/" + proxyProperties.getGroupId())){
                    if(curatorCrud.lock(ProxyConstants.GROUP_REGISTER_LOCK + proxyProperties.getGroupId())){
                        // 临时节点下不能创建子节点
                        curatorCrud.createPersistent(ProxyConstants.GROUP_REGISTER + "/" + proxyProperties.getGroupId(), "online");
                        curatorCrud.unlock(ProxyConstants.GROUP_REGISTER_LOCK + proxyProperties.getGroupId());
                    }
                    this.register();
                } else {
                    proxyProperties.getNodes().forEach(k -> {
                        try {
                            curatorCrud.createEphemeral(ProxyConstants.GROUP_REGISTER + "/" + proxyProperties.getGroupId() + "/"
                                    + (InetAddress.getLocalHost().getHostAddress()+":" + proxyProperties.getBindPort()), "online");
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                            throw new RuntimeException("register fail", e);
                        }
                    });
                }
            }else {
                Boolean lock = curatorCrud.lock(ProxyConstants.GROUP_REGISTER_LOCK);
                if(lock){
                    curatorCrud.createPersistent(ProxyConstants.GROUP_REGISTER, "lock");
                    curatorCrud.unlock(ProxyConstants.GROUP_REGISTER_LOCK);
                }
                this.register();
            }
        } catch (KeeperException e) {
            e.printStackTrace();
            throw new RuntimeException("register fail", e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("register fail", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("register fail", e);
        }
    }

}
