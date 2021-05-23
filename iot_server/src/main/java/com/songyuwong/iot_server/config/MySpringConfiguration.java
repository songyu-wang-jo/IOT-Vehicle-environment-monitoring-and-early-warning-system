package com.songyuwong.iot_server.config;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.websocket.server.ServerEndpointConfig;


/**
 * 以websocketConfig.java注册的bean是由自己管理的，需要使用配置托管给spring管理
 */
public class MySpringConfiguration extends ServerEndpointConfig.Configurator implements ApplicationContextAware {

    private static volatile BeanFactory context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        MySpringConfiguration.context = applicationContext;
    }

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return context.getBean(clazz);
    }

}
