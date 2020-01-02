package com.talkingdata.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.AttributeKey;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author yangtao
 */
@ChannelHandler.Sharable
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static AttributeKey<ChannelContext> channelContext = AttributeKey.newInstance("context");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        URI uri = new URI(request.uri());
        HttpMethod method = request.method();
        Map<String, String> paramMap = new HashMap<>();


        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer("hello 你好".getBytes("UTF-8")));
        response.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

        if (uri.getPath().equals("/favicon.ico")) {
            ctx.write(response);
            return;
        }
        if (method == HttpMethod.GET) {
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            decoder.parameters().entrySet().forEach(entry -> {
                // entry.getValue()是一个List, 只取第一个元素
                paramMap.put(entry.getKey(), entry.getValue().get(0));
            });
        }

        System.out.println("收到参数是 " + paramMap);
        if (paramMap.containsKey("newC")) {
            ctx.channel().attr(channelContext).set(new ChannelContext(UUID.randomUUID().toString()));
        }
        ChannelContext context = (ChannelContext) ctx.channel().attr(AttributeKey.valueOf("context")).get();
        System.out.println("上下文：" + context);
        System.out.println("channel：" + ctx.channel().hashCode());
        System.out.println("netty自身id:" + ctx.channel().id().asLongText());
        System.out.println("-------");

        response.headers().add("trace_id", ctx.channel().attr(channelContext).get().getId());

        ctx.writeAndFlush(response);
    }

    /**
     * Calls {@link ChannelHandlerContext#fireExceptionCaught(Throwable)} to forward
     * to the next {@link ChannelHandler} in the {@link io.netty.channel.ChannelPipeline}.
     * <p>
     * Sub-classes may override this method to change behavior.
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.writeAndFlush(cause);
    }

    /**
     * Calls {@link ChannelHandlerContext#fireChannelRegistered()} to forward
     * to the next {@link io.netty.channel.ChannelInboundHandler} in the {@link io.netty.channel.ChannelPipeline}.
     * <p>
     * Sub-classes may override this method to change behavior.
     *
     * @param ctx
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    /**
     * Calls {@link ChannelHandlerContext#fireChannelUnregistered()} to forward
     * to the next {@link io.netty.channel.ChannelInboundHandler} in the {@link io.netty.channel.ChannelPipeline}.
     * <p>
     * Sub-classes may override this method to change behavior.
     *
     * @param ctx
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }
}
