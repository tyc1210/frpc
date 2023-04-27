package com.tyc.frpc.client;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.tyc.frpc.client.config.FrpcClientConfig;
import com.tyc.frpc.client.config.FrpcClientNacosConfig;
import com.tyc.frpc.client.handler.HeartBeatHandler;
import com.tyc.frpc.client.handler.QuitHandler;
import com.tyc.frpc.client.handler.RpcResultHandler;
import com.tyc.frpc.client.nacos.NacosFactory;
import com.tyc.frpc.codec.DefaultLengthFieldBasedFrameDecoder;
import com.tyc.frpc.codec.MessageCodec;
import com.tyc.frpc.common.exception.RpcException;
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
    private final Logger log = LoggerFactory.getLogger(getClass());


    private static Channel channel;

    public static Channel getChannel() {
        return channel;
    }


    private AtomicBoolean started = new AtomicBoolean(false);
    private final FrpcClientConfig clientConfig;
    private final FrpcClientNacosConfig nacosConfig;

    public FrpcClientBootStrap(FrpcClientConfig clientConfig, FrpcClientNacosConfig nacosConfig) {
        this.clientConfig = clientConfig;
        this.nacosConfig = nacosConfig;
    }

    public void start(){
        if(started.compareAndSet(false,true)){
            checkNacos();
            init();
        }
    }

    public void checkNacos(){
        if(clientConfig != null && StringUtils.isNotBlank(clientConfig.getServiceName())){
            NacosFactory nacosFactory = new NacosFactory(nacosConfig);
            Instance instance = nacosFactory.subscribe(clientConfig.getServiceName());
            clientConfig.setIp(instance.getIp());
            clientConfig.setPort(instance.getPort());
        }
    }


    private void init(){
        Integer threadNum = clientConfig.getThreadNum();
        threadNum = threadNum == null || threadNum == 0 ? 8 : threadNum;
        NioEventLoopGroup loopGroup = new NioEventLoopGroup(threadNum);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(loopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
        /**
         * 添加各种 handler
         */
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        MessageCodec messageCodec = new MessageCodec();
        RpcResultHandler rpcResultHandler = new RpcResultHandler();

        try {
            bootstrap .handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch)
                        throws Exception {
                    ch.pipeline().addLast(new DefaultLengthFieldBasedFrameDecoder());
                    ch.pipeline().addLast(loggingHandler);
                    ch.pipeline().addLast(messageCodec);
                    ch.pipeline().addLast(new IdleStateHandler(0,3,0));
                    ch.pipeline().addLast(new IdleStateHandler(10,0,0));
                    ch.pipeline().addLast(new HeartBeatHandler());
                    ch.pipeline().addLast(new QuitHandler());
                    ch.pipeline().addLast(rpcResultHandler);
                }
            });
            channel = bootstrap.connect(clientConfig.getIp(), clientConfig.getPort()).sync().channel();
            log.info("frpc client start success ===>{}",clientConfig.getIp()+":"+clientConfig.getPort());
            channel.closeFuture().addListener(future -> {
                loopGroup.shutdownGracefully();
            });
        } catch (Throwable throwable) {
            throw new RpcException(throwable.getMessage());
        }
    }

}
