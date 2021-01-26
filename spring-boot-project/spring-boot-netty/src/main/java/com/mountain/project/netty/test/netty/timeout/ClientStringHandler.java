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
     *
     * @param ctx
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("handlerRemoved..." + System.currentTimeMillis());
    }

    /**
     * 当出现异常就关闭连接
     * 因于Netty的I/O异常或一个处理器实现的内部异常。多数情况下，捕捉到的异常应当被记录下来，
     * 并在这个方法中关闭这个channel通道。当然处理这种异常情况的方法实现可能因你的实际需求而有所不同，例如，在关闭这个连接之前你可能会发送一个包含了错误码的响应消息。
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("exceptionCaught...." + System.currentTimeMillis());
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 客户端退出通知事件（channelInactive）
     *
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("channelInactive..." + System.currentTimeMillis());
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