package com.panda.redis.proxy.base;

import com.panda.redis.base.api.Client;
import com.panda.redis.proxy.config.ProxyProperties;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 自定义Handler需要继承netty规定好的某个HandlerAdapter(规范)
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ProxyProperties proxyProperties;

    public NettyServerHandler() {
    }

    private static ExecutorService executor
            = Executors.newSingleThreadExecutor();

    /**
     * 读取客户端发送的数据
     *
     * @param ctx 上下文对象, 含有通道channel，管道pipeline
     * @param msg 就是客户端发送的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器读取线程 " + Thread.currentThread().getName());
        //Channel channel = ctx.channel();
        //ChannelPipeline pipeline = ctx.pipeline(); //本质是一个双向链接, 出站入站
        //将 msg 转成一个 ByteBuf，类似NIO 的 ByteBuffer
        ByteBuf buf = (ByteBuf) msg;
        String req = buf.toString(CharsetUtil.UTF_8);
//        byte[] req = new byte[buf.readableBytes()];
//        buf.readBytes(req);
        System.out.println("客户端发送消息是:" + req);
        Client jedis = new Client(proxyProperties.getNodes().get(0));
        Future<String> future = executor.submit(() -> {
            return jedis.send(req.getBytes());
        });
        String replay = future.get();
        //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
        ByteBuf respBuf = Unpooled.copiedBuffer((replay.trim()+"\r\n").getBytes(CharsetUtil.UTF_8));
        ctx.writeAndFlush(respBuf);
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
