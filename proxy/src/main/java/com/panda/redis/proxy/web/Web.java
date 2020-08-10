package com.panda.redis.proxy.web;

import com.panda.redis.proxy.base.NettyServer;
import com.panda.redis.proxy.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Web {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();
        NettyServer nettyServer = ctx.getBean(NettyServer.class);
        nettyServer.start();
    }

}
