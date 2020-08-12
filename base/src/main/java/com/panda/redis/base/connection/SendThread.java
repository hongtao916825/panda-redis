package com.panda.redis.base.connection;

import com.panda.redis.base.api.Client;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SendThread implements Runnable{

//    private static final Map<Long, Client> CLIENT_CACHE = new ConcurrentHashMap<>(10000);
    private static ThreadLocal<Client> CLIENT_CACHE = new ThreadLocal<>();
    private Channel channel;

    private String node;

    private byte[] req;

    public SendThread() {
    }

    public SendThread(Channel channel, String node, byte[] req) {
        this.channel = channel;
        this.node = node;
        this.req = req;
    }

    @Override
    public void run() {
        try {
            Client client = CLIENT_CACHE.get();
            if(client == null){
                client = new Client(node);
                CLIENT_CACHE.set(client);
            }
            String replay = client.send(req);
            //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
            ByteBuf respBuf = Unpooled.copiedBuffer((replay.trim() + "\r\n").getBytes(CharsetUtil.UTF_8));
            channel.writeAndFlush(respBuf);
//            channel.writeAndFlush(client.send2(req));
        } finally {
            //将内容返回到客户端
            channel.close(); //关闭连接
        }
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

}
