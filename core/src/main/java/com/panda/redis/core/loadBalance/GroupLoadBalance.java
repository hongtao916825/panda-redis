package com.panda.redis.core.loadBalance;

import com.panda.redis.core.pojo.GroupProxy;

/**
 * 选取proxy组的负载分流策略，也就是选取集群
 */
public interface GroupLoadBalance {

    GroupProxy chooseGroupServer();

}
