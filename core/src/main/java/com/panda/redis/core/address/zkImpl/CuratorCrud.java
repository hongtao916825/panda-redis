package com.panda.redis.core.address.zkImpl;

import com.panda.redis.core.callback.CallBackFunction;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.function.Function;

public class CuratorCrud {
//   private final String connectString = "192.168.0.31:2181,192.168.0.32:2181,192.168.0.33:2181";
   CuratorFramework cf ;
   ZkClient zkClient;
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
      zkClient = new ZkClient(connectString);
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

   public void lister(String path, CallBackFunction<ChangedParams> callback) throws Exception {
     zkClient.subscribeChildChanges(path, new IZkChildListener() {
         @Override
         public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
            callback.callBack(new ChangedParams(parentPath, currentChilds));
         }
      });
      //对父节点添加监听变化。
//      zkClient.subscribeDataChanges(path, new IZkDataListener() {
//         @Override
//         public void handleDataChange(String dataPath, Object data) throws Exception {
//            System.out.printf("变更的节点为:%s,%s", dataPath, data);
//         }
//
//         @Override
//         public void handleDataDeleted(String dataPath) throws Exception {
//            System.out.printf("删除的节点为:%s", dataPath);
//         }
//      });
//      //对父节点添加监听子节点变化。
//      zkClient.subscribeChildChanges(path, new IZkChildListener() {
//         @Override
//         public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
//            System.out.println("parentPath: " + parentPath + ",currentChilds:" + currentChilds);
//         }
//      });
//      //对父节点添加监听子节点变化。
//      zkClient.subscribeStateChanges(new IZkStateListener() {
//         @Override
//         public void handleStateChanged(Watcher.Event.KeeperState state) throws Exception {
//            if (state == Watcher.Event.KeeperState.SyncConnected) {
//               //当我重新启动后start，监听触发
//               System.out.println("连接成功");
//            } else if (state == Watcher.Event.KeeperState.Disconnected) {
//               System.out.println("连接断开");//当我在服务端将zk服务stop时，监听触发
//            } else {
//               System.out.println("其他状态" + state);
//            }
//         }
//
//         @Override
//         public void handleNewSession() throws Exception {
//            System.out.println("重建session");
//         }
//
//         @Override
//         public void handleSessionEstablishmentError(Throwable error) throws Exception {
//         }
//      });
   }

}

