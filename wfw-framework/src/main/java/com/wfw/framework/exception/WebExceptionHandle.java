package com.wfw.framework.exception;

import com.alibaba.fastjson.JSON;
import com.wfw.framework.web.WebApiController;
import com.wfw.framework.web.WebApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liaonanzhou
 * @date 2021-02-03 15:23
 * @description
 */
@ControllerAdvice
public class WebExceptionHandle extends WebApiController {

    private static final Logger logger = LoggerFactory.getLogger(WebExceptionHandle.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(HttpServletRequest request, Exception e) {
        WebApiResponse<Object> responseResult = new WebApiResponse<>();
        if (e instanceof ServiceException) {
            ServiceException serviceException = (ServiceException) e;
            logger.error("请求异常：url->{} , 参数->{}", request.getRequestURL(), JSON.toJSONString(request.getParameterMap()));
            responseResult.setCode(serviceException.getCode());
            responseResult.setMsg(serviceException.getMsg());
            return response(responseResult);
        }
        logger.error("异常堆栈：", e);
        responseResult.setCode(3000);
        responseResult.setMsg("服务异常");
        return response(responseResult);
    }

}
