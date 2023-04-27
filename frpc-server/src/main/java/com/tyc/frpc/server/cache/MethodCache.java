package com.tyc.frpc.server.cache;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存暴露给外部调用的方法
 *
 * @author tyc
 * @version 1.0
 * @date 2022-07-21 17:28:38
 */
public class MethodCache {
    public static Map<String, MethodContext> methodMap = new ConcurrentHashMap<>();

    public static class MethodContext{
        private Method method;
        private String beanName;
        private Object bean;

        public Object getBean() {
            return bean;
        }

        public Method getMethod() {
            return method;
        }


        public String getBeanName() {
            return beanName;
        }


        public MethodContext(Method method, String beanName,Object bean) {
            this.method = method;
            this.beanName = beanName;
            this.bean = bean;
        }
    }
}
