package com.mountain.project.bio.single;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * https://www.cnblogs.com/javazhiyin/p/11719132.html
 * 我们首先创建了一个服务端类，在类中实现实例化了一个SocketServer并绑定了8080端口。之后调用accept方法来接收连接请求，
 * 并且调用read方法来接收客户端发送的数据。最后将接收到的数据打印。
 * 完成了服务端的设计后，我们来实现一个客户端，首先实例化Socket对象，
 * 并且绑定ip为127.0.0.1（本机），端口号为8080，调用write方法向服务器发送数据。
 * 在服务器启动后，客户端还没有连接服务器时，
 * 服务器由于调用了accept方法，将一直阻塞，直到有客户端请求连接服务器。
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
                System.out.println("服务器已接收到连接请求...");
                System.out.println();
                System.out.println("服务器正在等待数据...");
                int result = socket.getInputStream().read(buffer);
                System.out.println();
                System.out.println("服务器已经接收到数据,result:" + result);
                System.out.println();
                String content = new String(buffer);
                System.out.println("接收到的数据:" + content);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}