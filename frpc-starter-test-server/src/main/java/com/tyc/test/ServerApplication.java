package com.tyc.test;

import com.tyc.frpc.starter.annoattion.EnableFrpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-24 10:29:30
 */
@SpringBootApplication
@EnableFrpc(basePackages = {"com.tyc.test"})
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class,args);
    }
}
