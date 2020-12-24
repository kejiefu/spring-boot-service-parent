package com.mountain.project.netty.test.netty.protobuf.server;


/**
 * @author kejiefu
 * @Description TODO
 * @Date 2020/12/24 15:49
 * @Created by kejiefu
 */
public class ImServerApp {

    public static void main(String[] args) {
        int port = 3333;
        new Thread(() -> {
            new ImServer().run(port);
        }).start();
    }

}
