package com.wfw.ribbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author liaonanzhou
 * @date 2021/2/3 16:40
 * @description x-> @EnableDiscoveryClient  表示用于发现 eureka 注册中心的微服务。
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
public class RibbonApplication {

    public static void main(String[] args) {
        SpringApplication.run(RibbonApplication.class, args);
    }

}
