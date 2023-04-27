package com.tyc.frpc.server.factory;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.tyc.frpc.server.config.FrpcServerNacosConfig;

import java.util.Properties;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-21 17:55:19
 */
public class NacosFactory {
    private final FrpcServerNacosConfig nacosConfigProperties;
    private NamingService namingService;

    public NacosFactory(FrpcServerNacosConfig nacosConfigProperties) {
        this.nacosConfigProperties = nacosConfigProperties;
        init();
    }

    private void init(){
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.SERVER_ADDR,nacosConfigProperties.getAddr());
        properties.setProperty(PropertyKeyConst.USERNAME,nacosConfigProperties.getUsername());
        properties.setProperty(PropertyKeyConst.PASSWORD,nacosConfigProperties.getPassword());
        try {
            // 获取nacos命名服务
            namingService = com.alibaba.nacos.api.NacosFactory.createNamingService(properties);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    public void register(String name,String ip,Integer port){
        Instance instance = new Instance();
        instance.setServiceName(name);
        instance.setIp(ip);
        instance.setPort(port);
        try {
            namingService.registerInstance(instance.getServiceName(),instance);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }
}
