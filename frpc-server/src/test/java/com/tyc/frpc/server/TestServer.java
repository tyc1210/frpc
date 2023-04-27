package com.tyc.frpc.server;

import com.tyc.frpc.server.config.FrpcServerConfig;
import com.tyc.frpc.server.config.FrpcServerNacosConfig;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-20 16:44:51
 */
public class TestServer {
    public static void main(String[] args) {
        FrpcServerConfig serverConfigProperties = new FrpcServerConfig();
        serverConfigProperties.setIp("127.0.0.1");
        serverConfigProperties.setPort(30001);
        serverConfigProperties.setName("frpc-server");
        FrpcServerNacosConfig nacosConfigProperties = new FrpcServerNacosConfig();
        nacosConfigProperties.setAddr("127.0.0.1:8848");
        nacosConfigProperties.setUsername("nacos");
        nacosConfigProperties.setPassword("123");
        FrpcServerBootStrap serverBootStrap = new FrpcServerBootStrap(serverConfigProperties, nacosConfigProperties);
        serverBootStrap.start();
    }
}
