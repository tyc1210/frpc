package com.tyc.test.controller;

import com.tyc.frpc.client.annotation.RpcReference;
import com.tyc.frpc.common.service.HelloSeivice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-24 14:45:13
 */
@RestController
@RequestMapping("hello")
public class TestController {
    @RpcReference
    private HelloSeivice helloSeivice;

    @GetMapping
    public String hello(){
        return helloSeivice.hello();
    }
}
