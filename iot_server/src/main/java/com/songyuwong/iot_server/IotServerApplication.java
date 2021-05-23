package com.songyuwong.iot_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
        {
                "com.songyuwong.iot_server.common",
                "com.songyuwong.iot_server.commonUtils",
                "com.songyuwong.iot_server.controller",
                "com.songyuwong.iot_server.dto",
                "com.songyuwong.iot_server.service",
                "com.songyuwong.iot_server.mapper",
                "com.songyuwong.iot_server.config"
        })
public class IotServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotServerApplication.class, args);
    }

}
