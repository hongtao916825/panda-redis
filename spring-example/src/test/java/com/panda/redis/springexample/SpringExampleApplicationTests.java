package com.panda.redis.springexample;

import com.panda.redis.core.context.RedisLoadBalance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Stack;

@SpringBootTest
class SpringExampleApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private RedisLoadBalance loadBalance;

    @Test
    void testRedisProxy(){
        for (int i = 0; i < 10; i++) {
            loadBalance.set("test.springboot.loadbalance" + i, "springboot-example" + i);
        }
    }

}
