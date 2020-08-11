package com.panda.redis.dash.dash.health.zkimpl;

import com.panda.redis.base.common.LogUtil;
import com.panda.redis.base.constants.ProxyConstants;
import com.panda.redis.dash.dash.health.HealthReport;
import com.panda.redis.dash.dash.util.DashContents;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ZookeeperHealthReport implements HealthReport {

    // 监听path列表
    private final static Set<String> REGISTED_PATH_CHANGED = new HashSet<>(100);

    // 监听data列表
    private final static Set<String> REGISTED_DATA_CHANGED = new HashSet<>(100);

    private String connectionString;

    private CuratorCrud curatorCrud;

    public ZookeeperHealthReport(String connectionString){
        this.connectionString = connectionString;
        curatorCrud = new CuratorCrud(connectionString);
    }

    @Override
    public void listener() {
        // 有递归的，所以只需要监听最上层节点即可
        try {
            curatorCrud.listerChildren(ProxyConstants.GROUP_REGISTER, (treeCacheEvent)->{
                TreeCacheEvent.Type eventType = treeCacheEvent.getType();
                ChildData data = treeCacheEvent.getData();
                switch (eventType) {
//                    case NODE_ADDED:
//                        LogUtil.info("NODE_ADDED : " + data.getPath() + "  数据:" + new String(data.getData()));
//                        add(data);
//                        break;
//                    case NODE_REMOVED:
//                        LogUtil.info("NODE_REMOVED : " + data.getPath() + "  数据:" + new String(data.getData()));
////                        remove(data);
//                        break;
                    case NODE_UPDATED:
                        LogUtil.info("NODE_UPDATED : " + data.getPath() + "  数据:" + new String(data.getData()));
                        change(data);
                        break;
                    default:
                        break;
                }
            });
        } catch (Exception e) {
            LogUtil.error("注册监听失败", e);
        }
    }

    private void change(ChildData data) {
        DashContents.sseCache.forEach((key,value) -> {
            try {
                value.send(new String(data.getData()));
            } catch (IOException e) {
                DashContents.sseCache.remove(key);
            }
        });
    }


}
