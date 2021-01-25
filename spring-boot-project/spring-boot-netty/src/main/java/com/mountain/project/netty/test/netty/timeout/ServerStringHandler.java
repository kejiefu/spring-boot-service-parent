package com.mountain.project.netty.test.netty.timeout;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerStringHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.err.println("server:" + msg.toString());
        ctx.writeAndFlush(msg.toString() + "你好");
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
        cause.printStackTrace();
        ctx.close();
    }

    public static void main(String[] args) {
        int port = 2222;
        new Thread(() -> {
            new ImServer().run(port);
        }).start();
    }
}