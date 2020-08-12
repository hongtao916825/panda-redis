package com.panda.redis.proxy.base;

import com.panda.redis.base.protocol.RedisReply;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RedisReplyEncoder extends MessageToByteEncoder<RedisReply> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RedisReply msg, ByteBuf out) throws Exception {
//        System.out.println("RedisReplyEncoder: " + msg);
        msg.write(out);
    }


}
