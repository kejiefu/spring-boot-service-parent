package com.mountain.project.netty.test.netty.protobuf.client;

import com.mountain.project.netty.test.netty.protobuf.MessageProto;
import io.netty.channel.Channel;

import java.util.UUID;

public class ImClientApp {

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 3333;
        Channel channel = new ImConnection().connect(host, port);
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        // protobuf
        MessageProto.Message.Builder builder = MessageProto.Message.newBuilder();
        builder.setId(id);
        builder.setContent("hello world");
        MessageProto.Message message = builder.build();
        System.out.println(System.currentTimeMillis());
        channel.writeAndFlush(message);
    }

}