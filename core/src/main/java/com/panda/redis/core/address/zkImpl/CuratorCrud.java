package com.panda.redis.core.address.zkImpl;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public class CuratorCrud {
//   private final String connectString = "192.168.0.31:2181,192.168.0.32:2181,192.168.0.33:2181";
   CuratorFramework cf ;
   public CuratorCrud(String connectString) {
      //1 重试策略：初试时间为1s 重试10次
      RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
      //2 通过工厂创建连接
      cf = CuratorFrameworkFactory.builder()
              .connectString(connectString)
              .sessionTimeoutMs(5000)
              .retryPolicy(retryPolicy)
//					.namespace("super")
              .build();
      //3 开启连接
      cf.start();
   }

   public List<String> getChildren(String path){
      try {
         List<String> children = cf.getChildren().forPath(path);
         return children;
      } catch (Exception e) {
         e.printStackTrace();
      }

      return  null;
   }

   public String createPersistent(String path,String  data){
      try {
         cf.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path,data.getBytes());
      } catch (Exception e) {
         e.printStackTrace();
      }

      return  null;
   }

   public String createEphemeral(String path,String  data){
      try {
         cf.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path,data.getBytes());
      } catch (Exception e) {
         e.printStackTrace();
      }

      return  null;
   }

   public String getData(String path){
      try {
         return new String(cf.getData().forPath(path));
      } catch (Exception e) {
         e.printStackTrace();
      }
      return  null;
   }


   public void delete(String path){
      try {
         cf.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
      } catch (Exception e) {
         e.printStackTrace();
      }

   }


   public void setData(String path,String  data){
      try {
         cf.setData().forPath(path,data.getBytes());
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public Boolean exists(String path) throws Exception {
      Stat stat = cf.checkExists().forPath(path);
      return stat != null;
   }

   public Boolean lock(String path) {
      try {
         cf.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, path.getBytes());
         return true;
      } catch (Exception e) {

      }
      return false;
   }

   public void unlock(String path) {
      try {
         cf.delete().forPath(path);
      } catch (Exception e) {
      }
   }

}
