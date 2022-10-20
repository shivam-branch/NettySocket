package com.socket.socketPractice;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public final class TcpServer {

    public static HashMap<String, SocketDataResource> map = new HashMap<>();


    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));

    @EventListener(ApplicationReadyEvent.class)
    public void run() throws Exception {

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            // the Decoder
                            p.addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
                            // the Encoder
                            p.addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));
                            // the handler: implement your logic here
                            p.addLast(new TcpServerHandler());
                        }
                    });
            // Start the server.
            ChannelFuture f = b.bind(PORT).sync();
            Channel channel = f.channel();


            // Wait until the server socket is closed.
            channel.closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void updateMap() {
        Long currentMilliSec = 1659080365406L;
        map.put("UP78", new SocketDataResource("UP78", currentMilliSec));
        new TcpServerHandler(map).sentData("UP78");
    }
}
