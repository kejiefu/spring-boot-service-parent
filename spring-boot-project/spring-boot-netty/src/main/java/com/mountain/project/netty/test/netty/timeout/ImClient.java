package com.mountain.project.netty.test.netty.timeout;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.TimeUnit;

public class ImClient {
    private Channel channel;
    public Channel connect(String host, int port) {
        doConnect(host, port);
        return this.channel;
    }
    private void doConnect(String host, int port) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    //Netty为超时控制封装了两个类ReadTimeoutHandler和WriteTimeoutHandler，ReadTimeoutHandler，用于控制读取数据的时候的超时，
                    // 如果在设置时间段内都没有数据读取了， 那么就引发超时，然后关闭当前的channel；会触发执行handlerRemoved
                    // WriteTimeoutHandler，用于控制数据输出的时候的超时，如果在设置时间段内都没有数据写了，
                    // 那么就超时。它们都是IdleStateHandler的子类。
                    ch.pipeline().addLast("readTimeOut", new ReadTimeoutHandler(8, TimeUnit.SECONDS));
                    ch.pipeline().addLast("decoder", new StringDecoder());
                    ch.pipeline().addLast("encoder", new StringEncoder());
                    ch.pipeline().addLast(new ClientStringHandler());
                }
            });
            ChannelFuture f = b.connect(host, port).sync();
            channel = f.channel();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}