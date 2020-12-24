package com.mountain.project.netty.test.netty.protobuf.server;

import com.mountain.project.netty.test.netty.protobuf.ConnectionPool;
import com.mountain.project.netty.test.netty.protobuf.MessageProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerPoHandlerProto extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        MessageProto.Message message = (MessageProto.Message) msg;
        if (ConnectionPool.getChannel(message.getId()) == null) {
            ConnectionPool.putChannel(message.getId(), ctx);
        }
        System.err.println("server msg" + message.getId() + "    " + message.getContent());
        ctx.writeAndFlush(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}