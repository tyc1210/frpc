package com.tyc.frpc.server.config;


/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2022-07-26 16:31:31
 */
public class FrpcServerNacosConfig {
    /**
     * nacos 地址
     */
    private String addr;
    private String username;
    private String password;

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
