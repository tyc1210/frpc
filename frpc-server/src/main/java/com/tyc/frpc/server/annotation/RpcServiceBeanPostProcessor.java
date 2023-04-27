package com.tyc.frpc.server.annotation;

import com.tyc.frpc.server.util.ScanUtil;
import com.tyc.frpc.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.Set;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-23 17:08:16
 */
public class RpcServiceBeanPostProcessor implements BeanDefinitionRegistryPostProcessor {
    private Logger log = LoggerFactory.getLogger(getClass());

    private Set<String> scanPaths;

    public RpcServiceBeanPostProcessor(Set<String> scanPaths) {
        this.scanPaths = scanPaths;
    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        for (String scanPath : scanPaths) {
            // 获取需要代理的类
            Set<Class<?>> classes = ScanUtil.scanAnnotationField(scanPath, RpcService.class);
            // 开始注入
            for (Class<?> aClass : classes) {
                GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                beanDefinition.setBeanClass(aClass);
                beanDefinition.setScope("singleton");
                beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(aClass);
                BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(beanDefinition, StringUtil.captureName(aClass.getSimpleName())),registry);
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
