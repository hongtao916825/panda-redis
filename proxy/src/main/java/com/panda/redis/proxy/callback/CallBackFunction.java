package com.panda.redis.proxy.callback;

@FunctionalInterface
public interface CallBackFunction<T> {

    T callBack();

}
