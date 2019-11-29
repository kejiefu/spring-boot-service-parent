package com.mountain.project.bio.multi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
在刚才的服务器实现中，我们实现的是单线程版的BIO服务器，
不难看出，单线程版的BIO并不能处理多个客户端的请求，那么如何能使BIO处理多个客户端请求呢。
其实不难想到，我们只需要在每一个连接请求到来时，创建一个线程去执行这个连接请求，
就可以在BIO中处理多个客户端请求了，这也就是为什么BIO的其中一条概念是服务器实现模式为一个连接一个线程，
即客户端有连接请求时服务器端就需要启动一个线程进行处理。

多线程BIO服务器虽然解决了单线程BIO无法处理并发的弱点，但是也带来一个问题：如果有大量的请求连接到我们的服务器上，
但是却不发送消息，那么我们的服务器也会为这些不发送消息的请求创建一个单独的线程，那么如果连接数少还好，
连接数一多就会对服务端造成极大的压力。所以如果这种不活跃的线程比较多，我们应该采取单线程的一个解决方案，
但是单线程又无法处理并发，这就陷入了一种很矛盾的状态，于是就有了NIO。
 */
public class Server {
    public static void main(String[] args) {
        byte[] buffer = new byte[1024];
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("服务器已启动并监听8080端口");
            while (true) {
                System.out.println();
                System.out.println("服务器正在等待连接...");
                Socket socket = serverSocket.accept();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("服务器已接收到连接请求...");
                        System.out.println();
                        System.out.println("服务器正在等待数据...");
                        try {
                            socket.getInputStream().read(buffer);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName() + "服务器已经接收到数据");
                        System.out.println();
                        String content = new String(buffer);
                        System.out.println("接收到的数据:" + content);
                    }
                }).start();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}