package com.tyc.frpc.server;

import com.tyc.frpc.codec.DefaultLengthFieldBasedFrameDecoder;
import com.tyc.frpc.codec.MessageCodec;
import com.tyc.frpc.server.config.FrpcServerConfig;
import com.tyc.frpc.server.config.FrpcServerNacosConfig;
import com.tyc.frpc.server.factory.NacosFactory;
import com.tyc.frpc.server.handler.PingHandler;
import com.tyc.frpc.server.handler.QuitHandler;
import com.tyc.frpc.server.handler.RpcRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-21 17:53:29
 */
public class FrpcServerBootStrap {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private FrpcServerConfig frpcConfigProperties;
    private FrpcServerNacosConfig nacosConfigProperties;

    public FrpcServerBootStrap(FrpcServerConfig frpcConfigProperties, FrpcServerNacosConfig nacosConfigProperties) {
        this.frpcConfigProperties = frpcConfigProperties;
        this.nacosConfigProperties = nacosConfigProperties;
    }

    private ChannelFuture future = null;

    public void start(){
        init();
    }


    /**
     * 服务注册到注册中心
     */
    public void registServer(){
        NacosFactory nacosFactory = new NacosFactory(nacosConfigProperties);
        if(null != nacosConfigProperties && StringUtils.isNotBlank(nacosConfigProperties.getAddr())){
            nacosFactory.register(frpcConfigProperties.getName(),frpcConfigProperties.getIp(),frpcConfigProperties.getPort());
        }
    }


    public void init() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 日志
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        // 自定义编解码器
        MessageCodec messageCodec = new MessageCodec();
        // 处理消息类型为RpcRequest的handler
        RpcRequestHandler rpcRequestHandler = new RpcRequestHandler();
        try {
            bootstrap.group(bossGroup,workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.SO_BACKLOG,128)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            // 处理粘包半包
                            ch.pipeline().addLast(new DefaultLengthFieldBasedFrameDecoder());
                            ch.pipeline().addLast(loggingHandler);
                            ch.pipeline().addLast(messageCodec);
                            // 心跳检测 判断读/写 时间过长
                            // 多少秒未收到数据
                            // 多少秒未进行写数据
                            // 读写都没有的空闲时间
                            // 触发对应的时间
                            ch.pipeline().addLast(new IdleStateHandler(5,0,0));
                            ch.pipeline().addLast(new ChannelDuplexHandler(){
                                @Override
                                public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                    IdleStateEvent event = (IdleStateEvent)evt;
                                    if(event.state() == IdleState.READER_IDLE){
                                        log.info("触发读空闲事件，释放资源");
                                        // 释放资源
                                        ctx.channel().close();
                                    }
                                }
                            });
                            // 处理连接断开
                            ch.pipeline().addLast(new PingHandler());
                            ch.pipeline().addLast(new QuitHandler());
                            ch.pipeline().addLast(rpcRequestHandler);
                        }
                    });
            future = bootstrap.bind(frpcConfigProperties.getPort()).sync();
            log.info("frpc server start success,port:{}",frpcConfigProperties.getPort());
            registServer();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
