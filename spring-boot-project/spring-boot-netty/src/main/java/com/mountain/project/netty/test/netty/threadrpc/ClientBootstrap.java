package com.mountain.project.netty.test.netty.threadrpc;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientBootstrap {


    public static final String providerName = "HelloService#hello#";

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        // 创建一个代理对象
        RpcConsumer consumer = new RpcConsumer();
        HelloService service = (HelloService) consumer
                .createProxy(HelloService.class, providerName);
        for (int i = 0; i < 2; i++) {
            executorService.submit(() -> {
                try {
                    //Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String msg = "are you ok ?" + System.currentTimeMillis() + "  " + new Random().nextInt(1000000);
                System.out.println("msg:" + Thread.currentThread().getName() + ":" + msg);
                String re = service.hello(msg);
                System.out.println("res:" + Thread.currentThread().getName() + ":" + re);
            });

        }
    }

}