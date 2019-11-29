package com.mountain.project.websocket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
 */
@Component
public class WebSocketServer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    @Value("${websocket.port}")
    private Integer port;

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
                    pipeline.addLast("http-codec", new HttpServerCodec()); // Http消息编码解码
                    pipeline.addLast("aggregator", new HttpObjectAggregator(65536)); // Http消息组装
                    pipeline.addLast("http-chunked", new ChunkedWriteHandler()); // WebSocket通信支持
                    pipeline.addLast("handler", new BananaWebSocketServerHandler()); // WebSocket服务端Handler
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
