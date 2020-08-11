package com.panda.redis.proxy.health;

public interface HealthReporter extends Runnable{

    void report();

}
