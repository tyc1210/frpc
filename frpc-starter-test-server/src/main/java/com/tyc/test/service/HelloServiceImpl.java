package com.tyc.test.service;

import com.tyc.frpc.common.service.HelloSeivice;
import com.tyc.frpc.server.annotation.RpcService;

import java.util.concurrent.TimeUnit;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-24 10:53:36
 */
@RpcService
public class HelloServiceImpl implements HelloSeivice {

    @Override
    public String hello(){
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "HELLO";
    }
}
