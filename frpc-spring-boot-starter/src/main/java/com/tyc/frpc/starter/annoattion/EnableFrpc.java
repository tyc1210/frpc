package com.tyc.frpc.starter.annoattion;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Configuration
@Retention(RetentionPolicy.RUNTIME)
@FrpcComponentScan
public @interface EnableFrpc {
    @AliasFor(annotation = FrpcComponentScan.class, attribute = "value")
    String[] value() default {};

    @AliasFor(annotation = FrpcComponentScan.class, attribute = "basePackages")
    String[] basePackages() default {};

    @AliasFor(annotation = FrpcComponentScan.class, attribute = "basePackageClasses")
    Class<?>[] basePackageClasses() default {};
}
