package com.tyc.frpc.client.manager;

import com.alibaba.fastjson.JSONObject;
import com.tyc.frpc.client.FrpcClientBootStrap;
import com.tyc.frpc.codec.message.RpcResult;
import com.tyc.frpc.common.exception.RpcException;
import io.netty.channel.Channel;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-05-05 16:21:19
 */
public class PendingFutureManager {
    public static Map<Integer, Promise<RpcResult>> map = new ConcurrentHashMap<>();

    public static Object pendingResult(Integer requestId, Channel channel) throws InterruptedException {
        // 准备Promise获取结果 传入的eventLoop代表若异步获取Promise结果由哪个线程处理
        DefaultPromise<RpcResult> promise = new DefaultPromise<>(channel.eventLoop());
        map.put(requestId,promise);
        // 创建一个定时任务，在 n 毫秒后检查 promise 是否已经完成，如果没有完成则会触发 tryFailure() 方法并标记 promise 失败。
        ScheduledFuture<?> timeoutFuture = channel.eventLoop().schedule(() -> {
            if (!promise.isDone()) {
                promise.tryFailure(new TimeoutException("client pending result timeout"));
            }
        }, FrpcClientBootStrap.timeout, TimeUnit.MILLISECONDS);
        promise.addListener(future -> timeoutFuture.cancel(false));
        // 阻塞等待获取结果
        promise.await(FrpcClientBootStrap.timeout);
        if (promise.isSuccess()) {
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
                resultPromise.setFailure(new RuntimeException(JSONObject.toJSONString(rpcResult)));
            }
        }
    }
}
