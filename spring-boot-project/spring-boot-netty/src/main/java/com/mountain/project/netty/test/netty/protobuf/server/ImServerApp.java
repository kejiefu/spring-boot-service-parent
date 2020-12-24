package com.mountain.project.netty.test.netty.protobuf.server;

import org.springframework.boot.SpringApplication;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2020/12/24 15:49
 * @Created by kejiefu
 */
public class ImServerApp {

    public static void main(String[] args) {
        int port = 2222;
        new Thread(() -> {
            new ImServer().run(port);
        }).start();
    }

}
