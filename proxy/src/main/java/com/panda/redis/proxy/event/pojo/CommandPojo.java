package com.panda.redis.proxy.event.pojo;

import com.panda.redis.base.protocol.RedisCommand;

public class CommandPojo {

    /**
     * 指令
     */
    private RedisCommand redisCommand;

    public RedisCommand getRedisCommand() {
        return redisCommand;
    }

    public CommandPojo setRedisCommand(RedisCommand redisCommand) {
        this.redisCommand = redisCommand;
        return this;
    }

    public static CommandPojoBuilder createBuilder() {
        return new CommandPojoBuilder();
    }

    /**
     * 构造器
     */
    public static class CommandPojoBuilder {

        private RedisCommand redisCommand;

        public CommandPojoBuilder setRedisCommand(RedisCommand redisCommand) {
            this.redisCommand = redisCommand;
            return this;
        }

        public CommandPojo create(){
            return new CommandPojo().setRedisCommand(redisCommand);
        }

    }

}


