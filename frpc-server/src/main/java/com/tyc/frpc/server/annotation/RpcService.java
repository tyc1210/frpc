package com.tyc.frpc.server.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2022-07-21 14:03:39
 */
@Target({ElementType.TYPE})
@Service
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {

}
