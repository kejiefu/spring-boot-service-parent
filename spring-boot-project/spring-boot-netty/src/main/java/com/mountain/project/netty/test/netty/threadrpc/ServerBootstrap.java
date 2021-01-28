package com.mountain.project.netty.test.netty.threadrpc;


public class ServerBootstrap {

    public static void main(String[] args) {
        NettyServer.startServer("localhost", 8099);
    }
}