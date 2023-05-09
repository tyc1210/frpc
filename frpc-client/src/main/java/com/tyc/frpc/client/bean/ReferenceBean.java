package com.tyc.frpc.client.bean;

import com.alibaba.fastjson.JSONObject;
import com.tyc.frpc.client.FrpcClientBootStrap;
import com.tyc.frpc.client.handler.RpcResultHandler;
import com.tyc.frpc.client.manager.PendingFutureManager;
import com.tyc.frpc.codec.message.RpcRequest;
import com.tyc.frpc.codec.message.RpcResult;
import com.tyc.frpc.common.exception.RpcException;
import com.tyc.frpc.common.util.IDUtil;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.DefaultPromise;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 生成代理对象
 *
 * @author tyc
 * @version 1.0
 * @date 2022-07-22 13:46:22
 */
public class ReferenceBean<T> implements FactoryBean<T> {
    private Class<T> aClass;

    public ReferenceBean(Class<T> aClass) {
        this.aClass = aClass;
    }

    @Override
    public T getObject() throws Exception {
        // 返回目标对象的代理对象
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 调用 netty 客户端发送消息
                String classMethodName = new StringBuilder(method.getDeclaringClass().getName()).append(".").append(method.getName()).toString();
                RpcRequest rpcRequest = new RpcRequest(IDUtil.getLimitId(),classMethodName,args,FrpcClientBootStrap.serializeType);
                // 获取channel
                Channel channel = null;
                try {
                    channel = FrpcClientBootStrap.getChannel();
                    channel.writeAndFlush(rpcRequest);
                    return PendingFutureManager.pendingResult(rpcRequest.getId(), channel);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(null != channel){
                        // 归还连接
                        FrpcClientBootStrap.returnChannel(channel);
                    }
                }
                return null;
            }
        };
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class[]{aClass},handler);
    }

    @Override
    public Class<?> getObjectType() {
        return aClass;
    }

}
