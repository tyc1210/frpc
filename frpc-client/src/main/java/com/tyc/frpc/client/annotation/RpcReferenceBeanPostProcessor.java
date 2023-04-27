package com.tyc.frpc.client.annotation;

import com.tyc.frpc.client.bean.ReferenceBean;
import com.tyc.frpc.client.util.ScanUtil;
import com.tyc.frpc.common.util.StringUtil;
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
 * @date 2023-04-21 11:32:19
 */
public class RpcReferenceBeanPostProcessor implements BeanDefinitionRegistryPostProcessor {
    private Set<String> scanPaths;

    public RpcReferenceBeanPostProcessor(Set<String> scanPaths) {
        this.scanPaths = scanPaths;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        for (String scanPath : scanPaths) {
            // 获取需要代理的类
            Set<Class<?>> classes = ScanUtil.scanAnnotationField(scanPath, RpcReference.class);
            // 开始注入
            for (Class<?> aClass : classes) {
                GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                beanDefinition.setBeanClass(ReferenceBean.class);
                beanDefinition.setScope("singleton");
                beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(aClass);
                BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(beanDefinition, StringUtil.captureName(aClass.getSimpleName())),beanDefinitionRegistry);
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
