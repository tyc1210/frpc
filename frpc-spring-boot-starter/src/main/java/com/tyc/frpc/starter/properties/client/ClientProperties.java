package com.tyc.frpc.starter.properties.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-21 16:55:46
 */
@ConfigurationProperties(prefix = "frpc.client")
public class ClientProperties {
    /**
     * 服务提供者地址
     */
    private String ip = "127.0.0.1";

    /**
     * 服务启动者端口
     */
    private Integer port = 30001;

    /**
     * netty NioEventLoopGroup 中的线程数
     */
    private Integer threadNum;

    /**
     * 注册中心中服务提供者名称
     */
    private String serviceName = "frpc-server";

    /**
     * 是否可以
     */
    private boolean enabled = true;

    /**
     * 连接池最大数量
     */
    private Integer poolSize = 8;

    /**
     * 序列化方式：json|java|protobuf
     */
    private String serializeType = "json";

    public String getSerializeType() {
        return serializeType;
    }

    public void setSerializeType(String serializeType) {
        this.serializeType = serializeType;
    }

    public Integer getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(Integer threadNum) {
        this.threadNum = threadNum;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
