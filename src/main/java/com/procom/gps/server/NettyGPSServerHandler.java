package com.procom.gps.server;

import com.procom.gps.GPSBucketsCollector;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Evgeniy Surovskiy
 */
@ChannelHandler.Sharable
public class NettyGPSServerHandler extends ChannelInboundHandlerAdapter
{
    private final static GPSBucketsCollector gpsBucketsCollector = new GPSBucketsCollector();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        gpsBucketsCollector.processNewBucket((String) msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
