package com.mountain.project.netty.test.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientStringHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("client:" + msg.toString());
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 2222;
        Channel channel = new ImConnection().connect(host, port);
        channel.writeAndFlush("kejiefu");
    }
}