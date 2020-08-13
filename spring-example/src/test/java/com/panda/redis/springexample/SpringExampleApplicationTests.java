package com.panda.redis.springexample;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.context.RedisLoadBalance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

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
//        for (int i = 0; i < 10; i++) {
//            loadBalance.set("test.springboot.loadbalance" + i, "springboot-example" + i);
//        }
        Jedis client = new Jedis("127.0.0.1", 6067);
        System.out.println(client.get("test.springboot.loadbalance4"));
    }

}
