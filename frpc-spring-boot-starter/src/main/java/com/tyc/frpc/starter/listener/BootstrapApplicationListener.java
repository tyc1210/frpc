package com.tyc.frpc.starter.listener;

import com.alibaba.fastjson.JSONObject;
import com.tyc.frpc.client.FrpcClientBootStrap;
import com.tyc.frpc.client.bean.ReferenceBean;
import com.tyc.frpc.server.FrpcServerBootStrap;
import com.tyc.frpc.server.annotation.RpcService;
import com.tyc.frpc.server.cache.MethodCache;
import com.tyc.frpc.server.util.ScanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-23 17:03:15
 */
public class BootstrapApplicationListener extends OneTimeExecutionApplicationContextEventListener implements Ordered {
    private Set<String> scanPaths;

    public BootstrapApplicationListener(Set<String> scanPaths) {
        this.scanPaths = scanPaths;
    }

    private final Logger log = LoggerFactory.getLogger(getClass());
    private ExecutorService executors = Executors.newSingleThreadExecutor();

    @Override
    public void onApplicationContextEvent(ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            onContextRefreshedEvent((ContextRefreshedEvent) event);
        } else if (event instanceof ContextClosedEvent) {
            onContextClosedEvent((ContextClosedEvent) event);
        }
    }

    private void onContextRefreshedEvent(ContextRefreshedEvent event){
        ApplicationContext applicationContext = event.getApplicationContext();
        // 将带有@RpcService注解的bean放入缓存
//        putRpcServiceBean(scanPaths,applicationContext);
        // 根据容器中的bean判断 启动server、client 服务
        boolean empty = MethodCache.methodMap.isEmpty();
        String[] rpcFactoryBeans = applicationContext.getBeanNamesForType(ReferenceBean.class);
        if(rpcFactoryBeans.length != 0){
            executors.execute(()->{
                log.info("start frpc client .");
                FrpcClientBootStrap bootStrap = applicationContext.getBean(FrpcClientBootStrap.class);
                bootStrap.start();
            });
        }
        if(!empty){
            executors.execute(()->{
                log.info("start frpc server .");
                FrpcServerBootStrap bootStrap = applicationContext.getBean(FrpcServerBootStrap.class);
                bootStrap.start();
            });
        }
    }

    private void putRpcServiceBean(Set<String> scanPaths,ApplicationContext applicationContext) {
        for (String scanPath : scanPaths) {
            Set<Class<?>> classes = ScanUtil.scanAnnotation(scanPath, RpcService.class);
            for (Class<?> aClass : classes) {
                String[] beanNames = applicationContext.getBeanNamesForType(aClass);
                for (String beanName : beanNames) {
                    log.debug("获取到需要供远程调用的bean，beanName:{}",beanName);
                    Class<?> classBean = applicationContext.getType(beanName);
                    Method[] methods = classBean.getMethods();
                    for (Method method : methods) {
                        try {
                            // getDeclaringClass返回声明此Method的Class对象
                            if(method.getDeclaringClass() == aClass){
                                log.debug("目标方法：{}，放入到缓存",method.getName());
                                // 测试方法调用
                                Object bean = applicationContext.getBean(beanName, aClass);
                                Object result = method.invoke(bean);
                                log.info(JSONObject.toJSONString(result));
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
            }
        }
    }

    private void onContextClosedEvent(ContextClosedEvent event){
        // todo 容器关闭做些善后工作
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
