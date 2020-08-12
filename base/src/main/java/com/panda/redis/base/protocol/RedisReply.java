package com.panda.redis.base.protocol;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

public interface RedisReply<T> {

    byte[] CRLF = new byte[] { '\r', '\n' };

    T data();

    void write(ByteBuf out) throws IOException;

}

