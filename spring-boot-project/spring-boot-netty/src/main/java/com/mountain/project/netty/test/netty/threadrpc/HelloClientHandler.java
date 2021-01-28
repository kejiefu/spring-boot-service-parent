package com.mountain.project.netty.test.netty.threadrpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @author kejiefu
 */
public class HelloClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;
    private String result;
    private String para;

    /**
     * 建立链接触发
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("channelActive..." + System.currentTimeMillis());
        context = ctx;
    }

    /**
     * 收到服务端数据，唤醒等待线程
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("channelRead..." + System.currentTimeMillis());
        result = msg.toString();
        notify();
    }

    /**
     * 写出数据，开始等待唤醒
     */
    @Override
    public synchronized Object call() throws InterruptedException {
        System.out.println("call..." + System.currentTimeMillis());
        context.writeAndFlush(para);
        wait();
        System.out.println("call..." + System.currentTimeMillis());
        return result;
    }

    void setPara(String para) {
        this.para = para;
    }
}