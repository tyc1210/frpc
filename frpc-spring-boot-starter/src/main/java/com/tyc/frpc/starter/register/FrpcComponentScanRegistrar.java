package com.tyc.frpc.starter.register;

import com.tyc.frpc.client.annotation.RpcReferenceBeanPostProcessor;
import com.tyc.frpc.starter.annoattion.FrpcComponentScan;
import com.tyc.frpc.starter.listener.BootstrapApplicationListener;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import com.tyc.frpc.common.util.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-21 09:41:42
 */
public class FrpcComponentScanRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Set<String> packagesToScan = getPackagesToScan(importingClassMetadata);
//        registerServiceAnnotationBeanPostProcessor(packagesToScan, registry);
        registerReferenceAnnotationBeanPostProcessor(packagesToScan,registry);
        registerBootstrapApplicationListener(packagesToScan,registry);
    }

    private void registerBootstrapApplicationListener(Set<String> packagesToScan, BeanDefinitionRegistry registry){
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(BootstrapApplicationListener.class);
        beanDefinition.setScope("singleton");
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(packagesToScan);
        BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(beanDefinition, StringUtil.captureName(BootstrapApplicationListener.class.getSimpleName())),registry);
    }

    /**
     * 注册处理@Service注解的后置处理器 ServiceAnnotationBeanPostProcessor
     * @param packagesToScan
     * @param registry
     */
//    private void registerServiceAnnotationBeanPostProcessor(Set<String> packagesToScan, BeanDefinitionRegistry registry) {
//        registerInfrastructureBean(registry,
//                RpcServiceBeanPostProcessor.class.getName(), RpcServiceBeanPostProcessor.class);
//    }

    /**
     * 注册处理@Referance注解的后置处理器
     * @param registry
     */
    private void registerReferenceAnnotationBeanPostProcessor(Set<String> packagesToScan,BeanDefinitionRegistry registry) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(RpcReferenceBeanPostProcessor.class);
        beanDefinition.setScope("singleton");
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(packagesToScan);
        BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(beanDefinition, StringUtil.captureName(RpcReferenceBeanPostProcessor.class.getSimpleName())),registry);
    }

    public static boolean registerInfrastructureBean(BeanDefinitionRegistry beanDefinitionRegistry,
                                                     String beanName,
                                                     Class<?> beanType) {

        boolean registered = false;
        if (!beanDefinitionRegistry.containsBeanDefinition(beanName)) {
            RootBeanDefinition beanDefinition = new RootBeanDefinition(beanType);
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
            registered = true;
        }

        return registered;
    }

    private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                metadata.getAnnotationAttributes(FrpcComponentScan.class.getName()));
        String[] basePackages = attributes.getStringArray("basePackages");
        Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
        String[] value = attributes.getStringArray("value");
        // Appends value array attributes
        Set<String> packagesToScan = new LinkedHashSet<String>(Arrays.asList(value));
        packagesToScan.addAll(Arrays.asList(basePackages));
        for (Class<?> basePackageClass : basePackageClasses) {
            packagesToScan.add(ClassUtils.getPackageName(basePackageClass));
        }
        if (packagesToScan.isEmpty()) {
            return Collections.singleton(ClassUtils.getPackageName(metadata.getClassName()));
        }
        return packagesToScan;
    }
}
