package com.tyc.test.service;

import com.tyc.frpc.common.service.HelloSeivice;
import com.tyc.frpc.server.annotation.RpcService;

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
        return "HELLO";
    }
}
