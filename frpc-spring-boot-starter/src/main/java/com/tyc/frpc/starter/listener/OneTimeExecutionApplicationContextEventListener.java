package com.tyc.frpc.starter.listener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;

import java.util.Objects;

/**
 * 类描述
 *
 * @author tyc
 * @version 1.0
 * @date 2023-04-23 17:02:48
 */
public abstract class OneTimeExecutionApplicationContextEventListener implements ApplicationListener, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (isOriginalEventSource(event) && event instanceof ApplicationContextEvent) {
            onApplicationContextEvent((ApplicationContextEvent) event);
        }
    }

    protected abstract void onApplicationContextEvent(ApplicationContextEvent event);

    private boolean isOriginalEventSource(ApplicationEvent event) {
        return (applicationContext == null) // Current ApplicationListener is not a Spring Bean, just was added
                // into Spring's ConfigurableApplicationContext
                || Objects.equals(applicationContext, event.getSource());
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
