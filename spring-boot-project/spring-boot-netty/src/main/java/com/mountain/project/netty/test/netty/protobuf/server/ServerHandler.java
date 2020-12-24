package com.mountain.project.netty.test.netty.protobuf.server;

import com.mountain.project.netty.test.netty.protobuf.ConnectionPool;
import com.mountain.project.netty.test.netty.protobuf.MessageProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println(System.currentTimeMillis());
        MessageProto.Message message = (MessageProto.Message) msg;
        if (ConnectionPool.getChannel(message.getId()) == null) {
            ConnectionPool.putChannel(message.getId(), ctx);
        }
        System.err.println("server 接到消息:" + message.getId() + "    " + message.getContent());

        //信息会写给客户端
        MessageProto.Message.Builder builder = MessageProto.Message.newBuilder();
        builder.setId(String.valueOf(System.currentTimeMillis()));
        builder.setContent("很开心收到你的消息");
        MessageProto.Message message1 = builder.build();
        ctx.writeAndFlush(message1);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}