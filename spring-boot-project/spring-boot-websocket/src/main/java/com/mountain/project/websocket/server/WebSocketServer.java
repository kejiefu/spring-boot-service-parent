package com.mountain.project.websocket.server;

import com.mountain.project.websocket.handler.BananaWebSocketServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


/**
 * WebSocket服务
 * TCP黏包拆包
 * TCP是一个流协议，就是没有界限的一长串二进制数据。TCP作为传输层协议并不不了解上层业务数据的具体含义，它会根据TCP缓冲区的实际情况进行数据包的划分，
 * 所以在业务上认为是一个完整的包，可能会被TCP拆分成多个包进行发送，也有可能把多个小的包封装成一个大的数据包发送，这就是所谓的TCP粘包和拆包问题。
 *
 * socket偏向于底层，而netty是对socket的封装。
 */
@Component
public class WebSocketServer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    @Value("${websocket.port}")
    private Integer port;

    /**
     * 通过group方法关联了两个线程组，NioEventLoopGroup是用来处理I/O操作的线程池，
     * 第一个称为“boss”，用来accept客户端连接，第二个称为“worker”，处理客户端数据的读写操作。当然你也可以只用一个NioEventLoopGroup同时来处理连接和读写
     * childHandler用来配置具体的数据处理方式 ，可以指定编解码器，处理数据的Handler
     * ChannelHandlerContext 通道处理器上下文。
     * 当ChannelHandler添加到ChannelPipeline时，每一个处理器都会分配一个上下文与之绑定，生死不离。上下文可以自身处理器与其他的处理器进行交互，
     * 因为上下文并不会改变处理器本身，所以上下文是安全的。
     * @param var1
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments var1) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    //每实例一个客户端都会进来一次
                    ChannelPipeline pipeline = channel.pipeline();
                    // Http消息编码解码
                    pipeline.addLast("http-codec", new HttpServerCodec());
                    // Http消息组装
                    pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                    // WebSocket通信支持
                    pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                    // WebSocket服务端Handler
                    pipeline.addLast("handler", new BananaWebSocketServerHandler());
                    //（回车换行分包） 用LineBasedFrameDecoder 来解决需要在发送的数据结尾加上回车换行符，解决粘包拆包
                    pipeline.addLast(new LineBasedFrameDecoder(10240));
                    //ping
                    //pipeline.addLast("ping", new IdleStateHandler(60, 20, 60 * 10, TimeUnit.SECONDS));
                }
            });
            // 链接服务器
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            Channel channel = channelFuture.channel();
            logger.info("WebSocket 已经启动,端口:" + port + ".");
            //服务器同步连接断开时,这句代码才会往下执行
            channel.closeFuture().sync();
        } catch (Exception ex) {
            logger.error("run:", ex);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}
