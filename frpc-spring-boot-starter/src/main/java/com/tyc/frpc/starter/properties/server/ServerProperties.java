package com.tyc.frpc.starter.properties.server;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-20 16:18:07
 */
@ConfigurationProperties(prefix = "frpc.server")
public class ServerProperties {
    /**
     * 服务启动端口
     */
    private Integer port = 30001;
    /**
     * 服务注册ip
     */
    private String ip = "127.0.0.1";
    /**
     * 服务名称
     */
    private String name = "frpc-server";

    /**
     * 是否可用
     */
    private boolean enabled = true;

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
