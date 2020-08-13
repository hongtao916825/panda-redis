package com.panda.redis.proxy.base;

import com.panda.redis.base.protocol.BulkReply;
import com.panda.redis.base.protocol.RedisCommand;
import com.panda.redis.proxy.config.ProxyPool;
import com.panda.redis.proxy.config.ProxyProperties;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolAbstract;
import redis.clients.jedis.Protocol;

/**
 * 自定义Handler需要继承netty规定好的某个HandlerAdapter(规范)
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NettyServerHandler extends SimpleChannelInboundHandler<RedisCommand> {

    @Autowired
    private ProxyPool proxyPool;

    public NettyServerHandler() {
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RedisCommand cmd) throws Exception {
        try {
            ctx.channel().writeAndFlush(new BulkReply(proxyPool.send(cmd)));
        }finally {
            ctx.channel().close(); //关闭连接
        }

    }

    /**
     * 数据读取完毕处理方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ByteBuf buf = Unpooled.copiedBuffer("HelloClient".getBytes(CharsetUtil.UTF_8));
//        ctx.writeAndFlush(buf);
    }

    /**
     * 处理异常, 一般是需要关闭通道
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
