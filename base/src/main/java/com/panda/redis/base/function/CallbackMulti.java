package com.panda.redis.base.function;

@FunctionalInterface
public interface CallbackMulti<T, R, K> {

    T callbackMultiply(R r, K k);

    default void callThen(){

    }
}
