package com.tyc.frpc.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuitHandler extends ChannelInboundHandlerAdapter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    // 当连接断开时触发
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("断开连接");
        ctx.channel().close();
//        super.channelInactive(ctx);
    }

    // 发生异常断开
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("连接异常断开:{}",cause.getMessage());
        ctx.channel().close();
//        super.exceptionCaught(ctx, cause);
    }

}
