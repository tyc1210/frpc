package com.tyc.test.controller;

import com.tyc.test.service.HelloServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-24 11:30:54
 */
@RestController
public class TestController {
    @Autowired
    private HelloServiceImpl testService;

    @GetMapping
    public String hello(){
        return testService.hello();
    }
}
