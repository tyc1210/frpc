package com.tyc.frpc.client.handler;

import com.alibaba.fastjson.JSONObject;
import com.tyc.frpc.client.FrpcClientBootStrap;
import com.tyc.frpc.client.manager.PendingFutureManager;
import com.tyc.frpc.codec.message.RpcResult;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tyc
 */
@ChannelHandler.Sharable
public class RpcResultHandler extends SimpleChannelInboundHandler<RpcResult> {
    private final Logger log = LoggerFactory.getLogger(getClass());


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResult rpcResult) throws Exception {
        log.info("客户端收到消息：{}", JSONObject.toJSONString(rpcResult));
        PendingFutureManager.receiveResult(rpcResult);
    }

    /**
     * 当连接正常关闭断开时触发
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.warn("channelInactive:{}", ctx.channel().localAddress());
        FrpcClientBootStrap.invalidateChannel(ctx.channel());
        ctx.pipeline().remove(this);
        ctx.channel().close();
    }
}
