package com.panda.redis.core.callback;

@FunctionalInterface
public interface CallBackFunction<T> {

    void callBack(T t);

}
