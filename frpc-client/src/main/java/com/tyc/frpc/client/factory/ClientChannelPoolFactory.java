package com.tyc.frpc.client.factory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.ChannelPoolHandler;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.net.InetSocketAddress;

/**
 * BasePooledObjectFactory Apache Commons Pool 库中的一个抽象工厂类，用于管理连接对象的生命周期
 *
 * @author tyc
 * @version 1.0
 * @date 2023-05-05 17:25:51
 */
public class ClientChannelPoolFactory extends BasePooledObjectFactory<Channel> {
    private final ChannelPoolHandler  handler;
    private final Bootstrap bootstrap;
    private final String HOST;
    private final Integer PORT;

    public ClientChannelPoolFactory(ChannelPoolHandler handler, Bootstrap bootstrap,String host,Integer port) {
        this.handler = handler;
        this.bootstrap = bootstrap;
        this.HOST = host;
        this.PORT = port;
    }

    /**
     * 创建新的连接对象
     */
    @Override
    public Channel create() throws Exception {
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(HOST, PORT));
        Channel channel = future.sync().channel();
        handler.channelCreated(channel);
        return channel;
    }

    /**
     * 方法用于将连接对象包装成 PooledObject 对象
     */
    @Override
    public PooledObject<Channel> wrap(Channel channel) {
        return new DefaultPooledObject<>(channel);
    }

    /**
     * 销毁连接对象
     */
    @Override
    public void destroyObject(PooledObject<Channel> pooledObject) throws Exception {
        Channel channel = pooledObject.getObject();
        handler.channelReleased(channel);
        channel.close();
    }

    /**
     * 验证连接对象是否可用
     */
    @Override
    public boolean validateObject(PooledObject<Channel> pooledObject) {
        Channel channel = pooledObject.getObject();
        return channel.isActive();
    }
}
