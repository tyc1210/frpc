package com.tyc.frpc.starter.annoattion;

import com.tyc.frpc.starter.register.FrpcComponentScanRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({FrpcComponentScanRegistrar.class})
public @interface FrpcComponentScan {
    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};
}
