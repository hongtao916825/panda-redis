package test.redis.config;

import test.redis.config.bo.RedisBo;

public class TestRedis {

    public static void main(String[] args) {
        RedisBo redisBo = new RedisBo();
        redisBo.stringRedisTemplate.opsForValue().set("panda-redis:test1","12345");
    }

}
