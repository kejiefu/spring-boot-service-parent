package com.mountain.project.netty.test.netty.rpc;


public class ServerBootstrap {
    public static void main(String[] args) {
        NettyServer.startServer("localhost", 8088);
    }
}