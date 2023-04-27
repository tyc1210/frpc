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
    private String ip = "127.0.0.1";

    private Integer port = 30001;

    private Integer threadNum = 4;

    private String serviceName = "frpc-server";

    private boolean enabled = true;

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
