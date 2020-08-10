package com.panda.redis.base.function;

@FunctionalInterface
public interface CallBack<T> {

    T apply();

}
