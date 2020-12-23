package com.mountain.project.netty.test.netty.protobuf;

import java.util.UUID;

public class ImClientApp {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 2222;
        Channel channel = new ImConnection().connect(host, port);
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        // protobuf
        MessageProto.Message message = MessageProto.Message.newBuilder().setId(id).setContent("hello yinjihuan").build();
        channel.writeAndFlush(message);
    }
}