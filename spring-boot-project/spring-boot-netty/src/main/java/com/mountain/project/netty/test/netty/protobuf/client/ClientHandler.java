package com.mountain.project.netty.test.netty.protobuf.client;

import com.mountain.project.netty.test.netty.protobuf.MessageProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println(System.currentTimeMillis());
        MessageProto.Message message = (MessageProto.Message) msg;
        System.out.println("client接到消息:" + message.getId() + "   " + message.getContent());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}