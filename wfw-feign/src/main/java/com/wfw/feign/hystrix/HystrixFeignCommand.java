package com.wfw.feign.hystrix;

import com.netflix.hystrix.*;
import com.wfw.feign.feign.HystrixFeign;
import com.wfw.framework.exception.ServiceException;
import com.wfw.framework.web.WebApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liaonanzhou
 * @date 2021/8/4 10:39
 * @description 以线程池控制Hystrix 的熔断
 **/
public class HystrixFeignCommand extends HystrixCommand<WebApiResponse<?>> {
    private static final Logger logger = LoggerFactory.getLogger(HystrixFeignCommand.class);

    private final HystrixFeign hystrixFeign;

    private Integer xp;

    private HystrixFeignCommand(HystrixFeign hystrixFeign, Setter setter) {
        super(setter);
        this.hystrixFeign = hystrixFeign;
    }

    /**
     * THREAD 模式熔断
     * Hystrix 默认使用 THREAD 模式
     **/
    public static HystrixFeignCommand newThreadPoolCommand(HystrixFeign hystrixFeign) {
        return new HystrixFeignCommand(
                hystrixFeign,
                Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HystrixFeignCommand-THREAD"))
                        .andCommandKey(HystrixCommandKey.Factory.asKey("HystrixFeignCommand-THREAD"))
                        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("HystrixFeignCommand-THREAD"))
                        // 线程池控制
                        .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                                // 核心线程数
                                .withCoreSize(10)
                                // 最大线程数
                                .withMaximumSize(10)
                                // 最大队列数（线程池的队列数不能小于等于0）
                                .withMaxQueueSize(10)
                                // 线程空闲后保持存活时间
                                .withKeepAliveTimeMinutes(1)
                                // 置allowMaximumSizeToDivergeFromCoreSize值为true时，maximumSize才有作用
                                .withAllowMaximumSizeToDivergeFromCoreSize(true)
                                // 排队线程数量阈值，默认为5，达到时拒绝，如果配置了该选项，队列的大小是该队列
                                .withQueueSizeRejectionThreshold(3)
                        )
                        .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                                // 10s内发生10个请求才会进行统计，默认20
                                .withCircuitBreakerRequestVolumeThreshold(10)
                                // 失败次数达到50%发生熔断。默认50
                                .withCircuitBreakerErrorThresholdPercentage(50)
                                // 熔断3s之后进行恢复，重新统计数据，默认5s
                                .withCircuitBreakerSleepWindowInMilliseconds(3000)
                                // 超时处理
                                .withExecutionTimeoutEnabled(true)
                                .withExecutionTimeoutInMilliseconds(3000)
                        )
        );
    }

    /**
     * SEMAPHORE 模式熔断
     **/
    public static HystrixFeignCommand newSemaphoreCommand(HystrixFeign hystrixFeign) {
        return new HystrixFeignCommand(
                hystrixFeign,
                Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HystrixFeignCommand-SEMAPHORE"))
                        .andCommandKey(HystrixCommandKey.Factory.asKey("HystrixFeignCommand-SEMAPHORE"))
                        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("HystrixFeignCommand-SEMAPHORE"))
                        // 当模式为 SEMAPHORE 线程池配置不会生效
                        .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                                // 核心线程数
                                .withCoreSize(10)
                                // 最大线程数
                                .withMaximumSize(10)
                                // 最大队列数（线程池的队列数不能小于等于0）
                                .withMaxQueueSize(10)
                                // 线程空闲后保持存活时间
                                .withKeepAliveTimeMinutes(1)
                                // 置allowMaximumSizeToDivergeFromCoreSize值为true时，maximumSize才有作用
                                .withAllowMaximumSizeToDivergeFromCoreSize(true)
                                // 排队线程数量阈值，默认为5，达到时拒绝，如果配置了该选项，队列的大小是该队列
                                .withQueueSizeRejectionThreshold(3)
                        )
                        // 信号量控制
                        .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                                // 10s内发生10个请求才会进行统计，默认20
                                .withCircuitBreakerRequestVolumeThreshold(10)
                                // 失败次数达到50%发生熔断。默认50
                                .withCircuitBreakerErrorThresholdPercentage(50)
                                // 熔断3s之后进行恢复，重新统计数据，默认5s
                                .withCircuitBreakerSleepWindowInMilliseconds(3000)
                                // 超时处理
                                .withExecutionTimeoutEnabled(true)
                                .withExecutionTimeoutInMilliseconds(3000)
                                // fixme Semaphore 属性
                                // 最大并发 1，默认10
                                .withExecutionIsolationSemaphoreMaxConcurrentRequests(10)
                        )
        );
    }

    public void setXp(Integer xp) {
        this.xp = xp;
    }

    @Override
    protected WebApiResponse<?> run() {
        WebApiResponse<?> webApiResponse = hystrixFeign.pass(xp);
        logger.info("feign 返回：{}", webApiResponse);
        if (webApiResponse.isOk())
            return webApiResponse;
        else
            throw ServiceException.build(webApiResponse);
    }

    /**
     * #execute() or #queue() 执行报错就会调用这个方法
     **/
    @Override
    protected WebApiResponse<?> getFallback() {
        logger.error("熔断器执行报错");
        throw new ServiceException("熔断器执行报错");
    }
}
