package com.tyc.frpc.client.handler;

import com.tyc.frpc.client.FrpcClientBootStrap;
import com.tyc.frpc.codec.message.PingMessage;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 心跳监测处理
 *
 * @author tyc
 * @version 1.0
 * @date 2022-08-09 16:44:52
 */
public class HeartBeatHandler extends ChannelDuplexHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final PingMessage pingMessage = new PingMessage(FrpcClientBootStrap.serializeType);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if(event.state() == IdleState.WRITER_IDLE){
                log.debug("触发写空闲事件，发送心跳消息");
                ctx.writeAndFlush(pingMessage);
            }
            if(event.state() == IdleState.READER_IDLE){
                log.debug("触发读空闲事件，主动断开重连");
                ctx.channel().close();
            }
        }
    }
}
