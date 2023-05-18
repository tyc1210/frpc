package com.tyc.frpc.client;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.tyc.frpc.client.config.FrpcClientConfig;
import com.tyc.frpc.client.config.FrpcClientNacosConfig;
import com.tyc.frpc.client.factory.ClientChannelPoolFactory;
import com.tyc.frpc.client.handler.HeartBeatHandler;
import com.tyc.frpc.client.handler.RpcResultHandler;
import com.tyc.frpc.client.handler.SimpleChannelPoolHandler;
import com.tyc.frpc.client.nacos.NacosFactory;
import com.tyc.frpc.codec.DefaultLengthFieldBasedFrameDecoder;
import com.tyc.frpc.codec.MessageCodec;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-21 16:54:12
 */
public class FrpcClientBootStrap {
    private static final Logger log = LoggerFactory.getLogger(FrpcClientBootStrap.class);
    private AtomicBoolean started = new AtomicBoolean(false);
    private final FrpcClientConfig clientConfig;
    private final FrpcClientNacosConfig nacosConfig;
    public static String serializeType;
    public static Long timeout;
    public static GenericObjectPool<Channel> channelPool;

    public static void returnChannel(Channel channel){
        /**
         * 通知连接池管理器，连接已经被归还
         * 可能发生 Returned object not currently part of this pool，因为连接可能被置为无效
         */
        channelPool.returnObject(channel);
    }

    /**
     * 销毁（置为无效）池子中的一个对象
     * @param channel
     */
    public static void invalidateChannel(Channel channel){
        try {
            channelPool.invalidateObject(channel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Channel getChannel() {
        while (true) {
            Channel channel = null;
            try {
                // 从连接池中借用一个连接
                channel = channelPool.borrowObject();
                log.info("连接池中空闲连接数量：{}，正被使用数量：{}，等待连接数量：{}",channelPool.getNumIdle(),channelPool.getNumActive(),channelPool.getNumWaiters());
                return channel;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public FrpcClientBootStrap(FrpcClientConfig clientConfig, FrpcClientNacosConfig nacosConfig) {
        this.clientConfig = clientConfig;
        this.nacosConfig = nacosConfig;
        serializeType = clientConfig.getSerializeType();
        timeout = clientConfig.getTimeout();
    }

    public void start(){
        if(started.compareAndSet(false,true)){
            checkNacos();
            init();
        }
    }

    public void checkNacos(){
        if(nacosConfig != null && StringUtils.isNotBlank(nacosConfig.getAddr())){
            NacosFactory nacosFactory = new NacosFactory(nacosConfig);
            Instance instance = nacosFactory.subscribe(clientConfig.getServiceName());
            clientConfig.setIp(instance.getIp());
            clientConfig.setPort(instance.getPort());
        }
    }


    private void init(){
        Integer threadNum = clientConfig.getThreadNum();
        threadNum = threadNum == null || threadNum == 0 ? Runtime.getRuntime().availableProcessors() * 2 : threadNum;
        NioEventLoopGroup loopGroup = new NioEventLoopGroup(threadNum);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(loopGroup);
        bootstrap.channel(NioSocketChannel.class);
        // 设置连接超时时间
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, clientConfig.getConnectTimeout());

        try {
            bootstrap .handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch)
                        throws Exception {
                    /**
                     * 添加各种 handler
                     */
                    ch.pipeline().addLast(new DefaultLengthFieldBasedFrameDecoder());
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new MessageCodec());
                    ch.pipeline().addLast(new IdleStateHandler(0,3,0));
                    ch.pipeline().addLast(new IdleStateHandler(10,0,0));
                    ch.pipeline().addLast(new HeartBeatHandler());
                    ch.pipeline().addLast(new RpcResultHandler());
                }
            });
            // 初始化channelPool
            channelPool = new GenericObjectPool<>(new ClientChannelPoolFactory(new SimpleChannelPoolHandler(), bootstrap,clientConfig.getIp(),clientConfig.getPort()));
            channelPool.setMaxTotal(clientConfig.getPoolSize());
            // 设置定期清理任务
            channelPool.setTimeBetweenEvictionRunsMillis(5000);
            log.info("frpc client init success ===>{}",clientConfig.getIp()+":"+clientConfig.getPort());
//            channel = bootstrap.connect(clientConfig.getIp(), clientConfig.getPort()).sync().channel();
//            log.info("frpc client start success ===>{}",clientConfig.getIp()+":"+clientConfig.getPort());
//            channel.closeFuture().addListener(future -> {
//                loopGroup.shutdownGracefully();
//            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
