package com.tyc.frpc.server.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tyc.frpc.codec.message.RpcRequest;
import com.tyc.frpc.codec.message.RpcResult;
import com.tyc.frpc.common.exception.RpcException;
import com.tyc.frpc.server.cache.MethodCache;
import com.tyc.frpc.server.util.BeanUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 专门处理 RpcRequest 类型的消息
 */
@ChannelHandler.Sharable
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
        log.info("收到客户端数据：{}",JSONObject.toJSONString(rpcRequest));
        RpcResult rpcResult = null;
        try {
            String methodName = rpcRequest.getMethodName();
            MethodCache.MethodContext methodContext = MethodCache.methodMap.get(methodName);
            if(null == methodContext){
                throw new RpcException("请求方法不存在或未暴露:"+methodName);
            }
            Method method = methodContext.getMethod();
            Object[] rpcRequestArgs = rpcRequest.getArgs();
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] params = null;
            // 类型转换
            if(rpcRequestArgs != null && rpcRequestArgs.length > 0){
                params = new Object[rpcRequestArgs.length];
                for (int i = 0; i < rpcRequestArgs.length; i++) {
                    Class<?> parameterType = parameterTypes[i];
                    Object arg = rpcRequestArgs[i];
                    if(parameterType.isAssignableFrom(List.class)){
                        params[i] = JSONArray.parseArray(JSONArray.toJSONString(arg),parameterType);
                    }else if(parameterType.isAssignableFrom(String.class)){
                        params[i] = arg;
                    }else if(parameterType.isAssignableFrom(Long.class)){
                        params[i] = JSONObject.parseObject(JSONObject.toJSONString(arg),parameterType);
                    }
                }
            }
            Object result = method.invoke(methodContext.getBean(), params);
            String data = JSONObject.toJSONString(result);
            rpcResult = new RpcResult(0,rpcRequest.getId(),data);
            log.debug("返回客户端执行结果:{}",JSONObject.toJSONString(rpcResult));
        } catch (Exception e) {
            log.error("消息处理异常 ===》id:{},msg:{}",rpcRequest.getId(),e.getMessage());
            rpcResult = new RpcResult(-1,rpcRequest.getId(),e.getMessage());
        }
        ctx.channel().writeAndFlush(rpcResult);
//        ctx.channel().unsafe().flush();
    }
}
