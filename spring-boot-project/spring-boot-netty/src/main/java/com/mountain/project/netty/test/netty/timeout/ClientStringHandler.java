package com.mountain.project.netty.test.netty.timeout;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientStringHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("client:" + msg.toString());
    }

    /**
     * 这条连接上添加的所有的业务逻辑处理器都被移除掉后调用
     * @param ctx
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("handlerRemoved..." + System.currentTimeMillis());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("exceptionCaught....");
        cause.printStackTrace();
        ctx.close();
    }

    public static void main(String[] args) {
        try {
            String host = "127.0.0.1";
            int port = 2222;
            Channel channel = new ImClient().connect(host, port);
            channel.writeAndFlush("kejiefu");
            Thread.sleep(10000);
            System.out.println(System.currentTimeMillis());
            channel.writeAndFlush("kejiefu 10");
            System.out.println(System.currentTimeMillis());
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}