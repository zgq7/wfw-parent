package com.wfw.feign.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liaonanzhou
 * @date 2021/4/12 16:45
 * @description
 **/
@RestController
@RequestMapping(value = "/hystrix")
public class HystrixController {

    /**
     * 信号量隔离
     */
    @PostMapping(value = "/semaphore")
    public ResponseEntity<String> semaphore() {
        return null;
    }

    /**
     * 线程池隔离
     */
    @PostMapping(value = "/threadpool")
    public ResponseEntity<String> threadpool() {
        return null;
    }

}
