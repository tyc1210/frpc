package com.tyc.frpc.client.manager;

import com.alibaba.fastjson.JSONObject;
import com.tyc.frpc.codec.message.RpcResult;
import com.tyc.frpc.common.exception.RpcException;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-05-05 16:21:19
 */
public class PendingFutureManager {
    public static Map<Integer, Promise<RpcResult>> map = new ConcurrentHashMap<>();

    public static Object pendingResult(Integer requestId, EventLoop eventLoop, Method method) throws InterruptedException {
        // 准备Promise获取结果 传入的eventLoop代表若异步获取Promise结果由哪个线程处理
        DefaultPromise<RpcResult> promise = new DefaultPromise<>(eventLoop);
        map.put(requestId,promise);
        // 阻塞等待获取结果
        promise.await();
        if(promise.isSuccess()){
            RpcResult rpcResult = promise.getNow();
            return rpcResult.getResultData();
        }else {
            throw new RpcException(promise.cause().getMessage());
        }
    }

    public static void receiveResult(RpcResult rpcResult){
        Promise<RpcResult> resultPromise = map.remove(rpcResult.getId());
        if(null != resultPromise){
            if(rpcResult.getCode().equals(0)){
                resultPromise.setSuccess(rpcResult);
            }else {
                resultPromise.setFailure(new RuntimeException(JSONObject.toJSONString(rpcResult.getResultData())));
            }
        }
    }
}
