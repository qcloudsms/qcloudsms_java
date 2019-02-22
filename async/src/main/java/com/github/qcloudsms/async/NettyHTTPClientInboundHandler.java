package com.github.qcloudsms.async;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

import java.util.Map;
import java.util.Iterator;


public class NettyHTTPClientInboundHandler extends SimpleChannelInboundHandler<HttpObject> {

    private AttributeKey<RequestInfo> attrKey;

    public NettyHTTPClientInboundHandler(AttributeKey<RequestInfo> attrKey) {
        this.attrKey = attrKey;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        RequestInfo info = ctx.attr(attrKey).get();

        if (msg instanceof HttpResponse) {
            HttpResponse res = (HttpResponse) msg;
            info.response.setStatusCode(res.status().code())
                .setReason(res.status().reasonPhrase());

            if (!res.headers().isEmpty()) {
                Iterator<Map.Entry<String, String>> it = res.headers().iteratorAsString();
                while (it.hasNext()) {
                    Map.Entry<String, String> header = it.next();
                    info.response.addHeader(header.getKey(), header.getValue());
                }
            }
        }

        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            info.response.setBody(content.content().toString(CharsetUtil.UTF_8));

            if (content instanceof LastHttpContent) {
                info.tracing.setEndTime(System.currentTimeMillis());
                info.handler.onResponse(info.response, info.tracing);
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,
        java.lang.Object msg) throws java.lang.Exception {

        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
        java.lang.Throwable cause) throws java.lang.Exception {

        RequestInfo info = ctx.attr(attrKey).get();
        info.tracing.setEndTime(System.currentTimeMillis());
        info.handler.onError(cause, info.tracing);

        if (ctx.channel().isActive()) {
            ctx.channel()
                .writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
        }

        super.exceptionCaught(ctx, cause);
    }
}
