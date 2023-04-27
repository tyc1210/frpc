package com.tyc.frpc.client;

import com.tyc.frpc.client.config.FrpcClientConfig;
import com.tyc.frpc.client.config.FrpcClientNacosConfig;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-21 17:37:51
 */
public class TestClient {
    public static void main(String[] args) {
        FrpcClientConfig frpcClientConfig = new FrpcClientConfig();
        frpcClientConfig.setIp("127.0.0.1");
        frpcClientConfig.setPort(30001);
        FrpcClientNacosConfig frpcClientNacosConfig = new FrpcClientNacosConfig();
        FrpcClientBootStrap clientBootStrap = new FrpcClientBootStrap(frpcClientConfig, frpcClientNacosConfig);
        clientBootStrap.start();
    }
}
