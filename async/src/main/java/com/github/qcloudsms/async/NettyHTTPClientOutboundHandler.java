package com.github.qcloudsms.async;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.AttributeKey;


public class NettyHTTPClientOutboundHandler extends ChannelOutboundHandlerAdapter {

    private AttributeKey<RequestInfo> attrKey;

    public NettyHTTPClientOutboundHandler(AttributeKey<RequestInfo> attrKey) {
        super();
        this.attrKey = attrKey;
    }

    @Override
    public void write(ChannelHandlerContext ctx, java.lang.Object msg,
        ChannelPromise promise) throws java.lang.Exception {

        RequestInfo info = ctx.attr(attrKey).get();
        info.tracing.setWriteBeginTime(System.currentTimeMillis());

        super.write(ctx, msg, promise);
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws java.lang.Exception {
        RequestInfo info = ctx.attr(attrKey).get();
        if (info != null &&
            info.tracing.getReadBeginTime() < info.tracing.getWriteBeginTime()) {

            info.tracing.setReadBeginTime(System.currentTimeMillis());
        }

        super.read(ctx);
    }

}
