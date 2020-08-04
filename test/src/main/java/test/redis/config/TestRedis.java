package test.redis.config;

import test.redis.config.bo.RedisBo;

public class TestRedis {

    public static void main(String[] args) throws InterruptedException {
        RedisBo redisBo = new RedisBo();
        redisBo.stringRedisTemplate.opsForValue().set("panda-redis:test1","7777");
        System.out.println(redisBo.stringRedisTemplate.opsForValue().get("panda-redis:test2"));
    }

}
