package com.tyc.frpc.client.annotation;


import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义需要远程调用的注解
 * @author tcy
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Autowired
public @interface RpcReference {
}
