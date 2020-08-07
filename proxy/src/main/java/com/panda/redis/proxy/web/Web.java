package com.panda.redis.proxy.web;

import com.panda.redis.proxy.config.AppConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import com.panda.redis.proxy.base.NettyServerHandler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class Web {

    public static void main(String[] args) throws IOException, InterruptedException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();
    }

}
