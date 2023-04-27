package com.tyc.frpc.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-20 16:18:07
 */
public class FrpcServerConfig {
    /**
     * 服务启动端口
     */
    private Integer port;
    /**
     * 服务注册ip
     */
    private String ip;
    /**
     * 服务名称
     */
    private String name;

    /**
     * 是否可用
     */
    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
