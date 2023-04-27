package com.tyc.frpc.starter.config;

import com.tyc.frpc.client.FrpcClientBootStrap;
import com.tyc.frpc.client.config.FrpcClientConfig;
import com.tyc.frpc.client.config.FrpcClientNacosConfig;
import com.tyc.frpc.server.FrpcServerBootStrap;
import com.tyc.frpc.server.config.FrpcServerConfig;
import com.tyc.frpc.server.config.FrpcServerNacosConfig;
import com.tyc.frpc.starter.annoattion.EnableFrpc;
import com.tyc.frpc.starter.properties.client.ClientNacosProperties;
import com.tyc.frpc.starter.properties.client.ClientProperties;
import com.tyc.frpc.starter.properties.server.ServerNacosProperties;
import com.tyc.frpc.starter.properties.server.ServerProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-20 18:06:24
 */
@Configuration
@ConditionalOnBean(annotation = {EnableFrpc.class})
@EnableConfigurationProperties({ClientNacosProperties.class,ClientProperties.class,ServerProperties.class,ServerNacosProperties.class})
public class FrpcServerAutoConfiguration {
   @Autowired
   private ClientNacosProperties clientNacosProperties;

   @Autowired
   private ClientProperties clientProperties;

   @Autowired
   private ServerNacosProperties serverNacosProperties;

   @Autowired
   private ServerProperties serverProperties;

    @Bean
    @ConditionalOnMissingBean
    public FrpcServerBootStrap frpcServerBootStrap(){
        // 添加更多默认配置
        FrpcServerConfig frpcServerConfig = new FrpcServerConfig();
        FrpcServerNacosConfig frpcServerNacosConfig = new FrpcServerNacosConfig();
        BeanUtils.copyProperties(serverProperties,frpcServerConfig);
        BeanUtils.copyProperties(serverNacosProperties,frpcServerNacosConfig);
        return new FrpcServerBootStrap(frpcServerConfig,frpcServerNacosConfig);
    }

    @Bean
    @ConditionalOnMissingBean
    public FrpcClientBootStrap frpcClientBootStrap(){
        FrpcClientNacosConfig frpcClientNacosConfig = new FrpcClientNacosConfig();
        FrpcClientConfig frpcClientConfig = new FrpcClientConfig();
        BeanUtils.copyProperties(clientNacosProperties,frpcClientNacosConfig);
        BeanUtils.copyProperties(clientProperties,frpcClientConfig);
        return new FrpcClientBootStrap(frpcClientConfig,frpcClientNacosConfig);
    }

}
