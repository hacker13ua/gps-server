package com.procom.gps.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

/**
 * @author Evgeniy Surovskiy
 */
public class NettyGPSServer implements GPSServer
{
    @Override
    public void startServer(int port)
    {
        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(0);
        try
        {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception
                        {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
//                            p.addLast(new LoggingHandler(LogLevel.INFO));
                            p.addLast(new NettyGPSServerHandler());
                        }
                    });
            // Start the server.
            ChannelFuture f = bootstrap.bind(port).sync();
            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
