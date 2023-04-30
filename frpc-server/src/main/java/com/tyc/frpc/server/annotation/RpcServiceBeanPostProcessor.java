package com.tyc.frpc.server.annotation;

import com.alibaba.fastjson.JSONObject;
import com.tyc.frpc.server.cache.MethodCache;
import com.tyc.frpc.server.util.ScanUtil;
import com.tyc.frpc.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-23 17:08:16
 */
public class RpcServiceBeanPostProcessor implements BeanPostProcessor {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> aClass = bean.getClass();
        RpcService rpcService = aClass.getAnnotation(RpcService.class);
        if(null != rpcService){
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                try {
                    // getDeclaringClass返回声明此Method的Class对象
                    if(method.getDeclaringClass() == aClass){
                        log.debug("目标方法：{}，放入到缓存",method.getName());
//                        Object result = method.invoke(bean);
//                        log.info(JSONObject.toJSONString(result));
                        MethodCache.MethodContext methodContext = new MethodCache.MethodContext(method, beanName,bean);
                        Class<?>[] interfaces = aClass.getInterfaces();
                        String preName = interfaces != null && interfaces.length > 0 ? interfaces[0].getName() : aClass.getSimpleName();
                        String key = new StringBuilder(preName).append(".").append(method.getName()).toString();
                        MethodCache.methodMap.put(key,methodContext);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

}
