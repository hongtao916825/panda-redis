package com.panda.redis.base.function;

@FunctionalInterface
public interface CallBackFunction<T> {

    void callBack(T t);

}
