package com.panda.redis.core.address;

import com.panda.redis.base.common.LogUtil;

public interface ProxyLoader {

    default void initRegister(){

    }

    void loadAddress();

    void registerProxiesListener();

}
