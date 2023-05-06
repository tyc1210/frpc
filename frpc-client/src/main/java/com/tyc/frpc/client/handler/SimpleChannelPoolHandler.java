package com.tyc.frpc.client.handler;

import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPoolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 连接池管理器的回调接口，用于接收连接池的状态变化通知
 *
 * @author tyc
 * @version 1.0
 * @date 2023-05-05 17:48:17
 */
public class SimpleChannelPoolHandler implements ChannelPoolHandler {
    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 连接被归还到连接池中时，会调用
     */
    @Override
    public void channelReleased(Channel channel) throws Exception {
        log.info("channelReleased threadName:"+Thread.currentThread().getName());
    }

    /**
     * 当连接被从连接池中取出来时，会调用
     */
    @Override
    public void channelAcquired(Channel channel) throws Exception {
        log.info("channelAcquired threadName:"+Thread.currentThread().getName());
    }

    /**
     * 连接池需要新建一个连接时，调用
     */
    @Override
    public void channelCreated(Channel channel) throws Exception {
        log.info("channelCreated threadName:"+Thread.currentThread().getName());
    }

}
