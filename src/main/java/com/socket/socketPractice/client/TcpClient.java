package com.socket.socketPractice.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

public class TcpClient {

    String host;
    int port;
    ChannelHandler clientHandler;



    public TcpClient(String host, int port) {
        super();
        this.host = host;
        this.port = port;
    }

    protected void execute(){
        if(clientHandler == null){
            throw new IllegalArgumentException("clientHandler is NULL, please define a tcpClientChannelHandler !");
        }

        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    // Configure the connect timeout option.
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            // Decoder
                            p.addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));

                            // Encoder
                            p.addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));

                            // the handler for client
                            p.addLast(clientHandler);
                        }
                    });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }

    public TcpClient buildHandler(String message, CallbackProcessor asynchCall) throws Exception{
        clientHandler = new TcpClientHandler(message, asynchCall);
        return this;
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter room name : ");
        String message = scanner.next();
        new TcpClient("localhost",8007).buildHandler(message, rs -> {
            //UP78
            System.out.println(rs);
        }).execute();
    }


}
