package com.tyc.frpc.client.handler;

import com.alibaba.fastjson.JSONObject;
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

    public static Map<Integer, Promise<RpcResult>> map = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResult rpcResult) throws Exception {
        log.info("客户端收到消息：{}", JSONObject.toJSONString(rpcResult));
        Promise<RpcResult> promise = map.remove(rpcResult.getId());
        if(null != promise){
            if(rpcResult.getCode().equals(0)){
                promise.setSuccess(rpcResult);
            }else {
                promise.setFailure(new RuntimeException(rpcResult.getResultData()));
            }
        }
    }
}
