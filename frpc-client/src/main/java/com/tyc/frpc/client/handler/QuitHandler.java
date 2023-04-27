package com.tyc.frpc.client.handler;

import com.tyc.frpc.client.FrpcClientBootStrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author tyc
 */
public class QuitHandler extends ChannelInboundHandlerAdapter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private Integer clientWaitSeconds = 1;
    private final Integer  clientWaitMaxSeconds = 300;


    /**
     * 当连接正常关闭断开时触发
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.warn("channelInactive:{}", ctx.channel().localAddress());
        ctx.pipeline().remove(this);
        ctx.channel().close();
        reconnection();
    }


    /**
     * 发生异常断开
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof IOException) {
            log.warn("exceptionCaught:客户端[{}]和远程断开连接", ctx.channel().localAddress());
        } else {
            log.error(cause.getMessage());
        }
        ctx.pipeline().remove(this);
        ctx.channel().close();
        reconnection();
    }

    private void reconnection() {
        while (true){
            try {
                TimeUnit.SECONDS.sleep(clientWaitSeconds);
                clientWaitSeconds = clientWaitSeconds < clientWaitMaxSeconds / 2 ? clientWaitSeconds * 2 : clientWaitMaxSeconds;
                Channel channel = FrpcClientBootStrap.getChannel();
                if(channel.isActive()){
                    break;
                }else {
                }
            } catch (Throwable e) {
                log.error("client waitSeconds:{}, exception:{}",clientWaitSeconds,e.getMessage());
            }
        }
    }
}
