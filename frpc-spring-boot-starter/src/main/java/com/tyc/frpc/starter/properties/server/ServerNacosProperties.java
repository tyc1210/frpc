package com.tyc.frpc.starter.properties.server;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2022-07-26 16:31:31
 */
@ConfigurationProperties(prefix = "frpc.server.nacos")
public class ServerNacosProperties {
    /**
     * nacos 地址
     */
    private String addr = "127.0.0.1:8848";
    private String username = "";
    private String password = "";

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
