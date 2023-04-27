package com.tyc.frpc.client.nacos;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.tyc.frpc.client.config.FrpcClientNacosConfig;
import com.tyc.frpc.common.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-21 17:19:47
 */
public class NacosFactory {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final FrpcClientNacosConfig nacosConfig;

    // 配置管理
    private ConfigService configService;
    // 服务管理
    private NamingService namingService;

    public NacosFactory(FrpcClientNacosConfig nacosConfig) {
        this.nacosConfig = nacosConfig;
        init();
    }

    public void init(){
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.SERVER_ADDR,nacosConfig.getAddr());
        properties.setProperty(PropertyKeyConst.USERNAME,nacosConfig.getUsername());
        properties.setProperty(PropertyKeyConst.PASSWORD,nacosConfig.getPassword());
        try {
            namingService = com.alibaba.nacos.api.NacosFactory.createNamingService(properties);
        } catch (NacosException e) {
            e.printStackTrace();
            throw new RpcException("client nacos init error");
        }
    }

    public Instance subscribe(String serviceName){
        try {
            namingService.subscribe(serviceName,(event)->{
                if(event instanceof NamingEvent){
                    List<Instance> instances = ((NamingEvent) event).getInstances();
                    if(instances.isEmpty()){
                        throw new RpcException("client subscribe:"+serviceName+"has no Instance");
                    }
                }
            });
        } catch (NacosException e) {
            e.printStackTrace();
            throw new RpcException("client nacos init error");
        }
        return getOneHealthyInstance(serviceName);
    }

    /**
     * 注册服务
     */
    public void registerServer(Instance instance) throws Exception{
        namingService.registerInstance(instance.getServiceName(),instance);
    }

    /**
     * 删除服务
     */
    public void deleteServer(Instance instance) throws Exception{
        namingService.deregisterInstance(instance.getServiceName(),instance.getIp(),instance.getPort());
    }

    /**
     * 随机全部（有可能获取到的不健康）。
     * 可以按照自己的负载均衡算法进行调用。
     */
    public List<Instance> getAllServer(String serverName) throws Exception{
        return namingService.getAllInstances(serverName);
    }

    /**
     * 根据负载均衡算法获取一个健康的实例
     */
    public Instance getOneHealthyInstance(String serverName){
        try {
            return namingService.selectOneHealthyInstance(serverName);
        } catch (NacosException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
