package com.mountain.project.bio.multi;

import java.io.IOException;
import java.net.Socket;

/**
 * 从上文的运行结果中我们可以看到，服务器端在启动后，首先需要等待客户端的连接请求（第一次阻塞），
 * 如果没有客户端连接，服务端将一直阻塞等待，然后当客户端连接后，服务器会等待客户端发送数据（第二次阻塞），
 * 如果客户端没有发送数据，那么服务端将会一直阻塞等待客户端发送数据。
   服务端从启动到收到客户端数据的这个过程，将会有两次阻塞的过程。这就是BIO的非常重要的一个特点，
   BIO会产生两次阻塞，第一次在等待连接时阻塞，第二次在等待数据时阻塞。
 */
public class Consumer {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1",8080);
            socket.getOutputStream().write("向服务器发数据,你好！".getBytes());
            socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}