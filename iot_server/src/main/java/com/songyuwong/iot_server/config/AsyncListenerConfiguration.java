package com.songyuwong.iot_server.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


@Configuration
@EnableAsync
public class AsyncListenerConfiguration  {
    private static final Logger log = LoggerFactory.getLogger(AsyncListenerConfiguration.class);
    @Bean("taskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);//配置核心线程数
        executor.setMaxPoolSize(10);//配置核心线程数
        executor.setQueueCapacity(200);//配置队列容量
        executor.setKeepAliveSeconds(1000);//设置线程活跃时间
        executor.setThreadNamePrefix("myTaskAsyn-");//设置线程名
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); //设置拒绝策略
        executor.initialize();
        return executor;
    }
}