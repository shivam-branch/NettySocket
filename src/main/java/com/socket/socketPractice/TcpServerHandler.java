package com.socket.socketPractice;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.NoArgsConstructor;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@ChannelHandler.Sharable
@NoArgsConstructor
public class TcpServerHandler extends ChannelInboundHandlerAdapter {

    protected static final Map<String, ChannelHandlerContext> channels = new HashMap<>();

    public static HashMap<String, SocketDataResource> map = new HashMap<>();


    public TcpServerHandler(HashMap<String, SocketDataResource> map){
        this.map = map;
    }
    @Override
    public void channelRead(ChannelHandlerContext context, Object message) throws InterruptedException {

        String roomName = null;
        if (message instanceof String) {
            roomName = message.toString();
        }
        if (message instanceof byte[]) {
            roomName = new String((byte[]) message);
        }

        if (context.channel() != null && context.channel().remoteAddress() != null) {
            String channel = context.channel().remoteAddress().toString();
            channels.put(roomName, context);
        }

        System.out.println(roomName);



    }

    public void sentData(String roomName){
        ChannelHandlerContext context = channels.get(roomName);
        if(context != null) {
            context.channel().writeAndFlush(map.get(roomName).toString()+" \n");
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        System.out.println("channelRegistered "+ address.getAddress());
        isCatchedException = false;
    }
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        System.out.println("channelUnregistered "+ address.getAddress());
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("channelActive "+ctx.channel());
        ctx.channel().writeAndFlush("connected");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("channelInactive "+ctx.channel().remoteAddress());
    }

    boolean isCatchedException = false;

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
        //auto close the client connection after 60000 mili-seconds
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ctx.channel().writeAndFlush("close");
                ctx.close();
            }
        }, 30000);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}



