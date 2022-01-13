package com.wfw.feign.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.wfw.feign.feign.HystrixFeign;
import com.wfw.framework.exception.ServiceException;
import com.wfw.framework.web.WebApiResponse;
import org.springframework.stereotype.Service;

/**
 * @author liaonanzhou
 * @date 2022/1/13 17:18
 * @description
 **/
@Service
public class HystrixService {
    private final HystrixFeign hystrixFeign;

    public HystrixService(HystrixFeign hystrixFeign) {
        this.hystrixFeign = hystrixFeign;
    }

    /**
     * You can use @HystrixCommand annotation only on @service or @component class bean
     **/
    @HystrixCommand(
            groupKey = "hystrixFeign",
            threadPoolKey = "hystrixFeign-thread-pool",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "2"),
                    //@HystrixProperty(name = "maximumSize", value = "2"),
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "1"),
                    //@HystrixProperty(name = "maxQueueSize", value = "10"),
                    // 由于 maxQueueSize 值在线程池被创建后就固定了大小，如果需要动态修改队列长度的话可以设置此值，即使队列未满，队列内作业达到此值时同样会拒绝请求。此值默认是 5，所以有时候只设置了 maxQueueSize 也不会起作用。
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "5"),
                    // 此配置项指定了窗口的大小，单位是 ms，默认值是 1000，即一个滑动窗口默认统计的是 1s 内的请求数据。
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1000"),
                    // Hystrix 会将命令执行的结果类型都统计汇总到一块，给上层应用使用或生成统计图表，此配置项即指定了，生成统计数据流时滑动窗口应该拆分的桶数。
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "10"),
            },
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),
                    // 在使用统计信息做出打开/关闭决定之前必须在统计窗口内发出的请求数（设置滑动窗口大小为10s，那么10s内需要5个请求才会开启统计）
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
                    // 失败次数占据百分比达到多少开启熔断
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"),
                    // 熔断后多少ms关闭熔断（熔断后的快速失败时间窗口）
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "3000"),
                    // 请求超时开关（默认开启）
                    @HystrixProperty(name = "execution.timeout.enabled", value = "true"),
                    // 最大并发量（也就是Semaphore有多少把锁）
                    @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "3"),
            },
            fallbackMethod = "passFallback"
    )
    public WebApiResponse<String> pass(Integer xp) {
        return hystrixFeign.pass(xp);
    }

    public WebApiResponse<String> passFallback(Integer xp) {
        throw new ServiceException("熔断器执行报错");
    }

}
