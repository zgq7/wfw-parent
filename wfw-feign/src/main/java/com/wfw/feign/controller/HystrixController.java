package com.wfw.feign.controller;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.wfw.feign.feign.HystrixFeign;
import com.wfw.feign.hystrix.HystrixFeignCommand;
import com.wfw.framework.exception.ServiceException;
import com.wfw.framework.web.WebApiController;
import com.wfw.framework.web.WebApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author liaonanzhou
 * @date 2021/4/12 16:45
 * @description
 **/
@RestController
@RequestMapping(value = "/hystrix")
public class HystrixController extends WebApiController {
    private static final Logger logger = LoggerFactory.getLogger(HystrixController.class);

    private final HystrixFeign hystrixFeign;

    public HystrixController(HystrixFeign hystrixFeign) {
        this.hystrixFeign = hystrixFeign;
    }

    /**
     * 信号量隔离
     */
    @PostMapping(value = "/semaphore/{xp}")
    public ResponseEntity<String> semaphore(@PathVariable(name = "xp") Integer xp) {
        HystrixFeignCommand command = HystrixFeignCommand.newSemaphoreCommand(hystrixFeign);
        command.setXp(xp);
        return response(command.execute());
    }

    /**
     * 线程池隔离
     */
    @PostMapping(value = "/threadpool/{xp}")
    public ResponseEntity<String> threadpool(@PathVariable(name = "xp") Integer xp) {
        HystrixFeignCommand command = HystrixFeignCommand.newThreadPoolCommand(hystrixFeign);
        command.setXp(xp);
        return response(command.execute());
    }

    @ExceptionHandler(HystrixRuntimeException.class)
    public ResponseEntity<String> hystrixRuntimeException(HystrixRuntimeException e) {
        logger.error("hystrix error ：", e);
        if (HystrixRuntimeException.FailureType.SHORTCIRCUIT.equals(e.getFailureType())) {
            return response(WebApiResponse.build(new ServiceException(400_101, "哦豁，服务熔断了")));
        } else if (HystrixRuntimeException.FailureType.TIMEOUT.equals(e.getFailureType())) {
            return response(WebApiResponse.build(new ServiceException(400_102, "哦豁，调用超时")));
        } else if (HystrixRuntimeException.FailureType.COMMAND_EXCEPTION.equals(e.getFailureType())) {
            return response(WebApiResponse.build(new ServiceException(400_103, "哦豁，服务内部调用异常")));
        } else if (HystrixRuntimeException.FailureType.REJECTED_THREAD_EXECUTION.equals(e.getFailureType())) {
            return response(WebApiResponse.build(new ServiceException(400_104, "哦豁，服务器繁忙（thread pool队列不足）")));
        } else if (HystrixRuntimeException.FailureType.REJECTED_SEMAPHORE_EXECUTION.equals(e.getFailureType())) {
            return response(WebApiResponse.build(new ServiceException(400_105, "哦豁，服务器繁忙（semaphore 并发已达到最大）")));
        } else if (HystrixRuntimeException.FailureType.REJECTED_SEMAPHORE_FALLBACK.equals(e.getFailureType())) {
            return response(WebApiResponse.build(new ServiceException(400_105, "哦豁，回退调用次数过多")));
        } else {
            return response(WebApiResponse.build(new ServiceException(400_100, "熔断器执行异常")));
        }
    }

}
