package com.wfw.framework.exception;

import com.alibaba.fastjson.JSON;
import com.wfw.framework.web.WebApiController;
import com.wfw.framework.web.WebApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 400 参数解析失败异常拦截
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> messageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        return handleWebError(HttpStatus.BAD_REQUEST, e);
    }

    /**
     * 405 请求方法不支持异常拦截
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> methodNotFoundExceptionHandler(HttpRequestMethodNotSupportedException e) {
        return handleWebError(HttpStatus.METHOD_NOT_ALLOWED, e);
    }

    /**
     * 415 请求媒体类型不支持异常拦截
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> mediaTypeNotSupportedExceptionHandler(Exception e) {
        return handleWebError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e);
    }

    /**
     * 参数绑定异常
     */
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class, MissingServletRequestParameterException.class, ConstraintViolationException.class})
    public ResponseEntity<String> handleBindException(Exception e) {
        WebApiResponse<Void> webApiResponse = new WebApiResponse<>();
        webApiResponse.setCode(3000);

        List<FieldError> errorList = new ArrayList<>();
        if (e instanceof BindException) {
            errorList = ((BindException) e).getFieldErrors();
        } else if (e instanceof MethodArgumentNotValidException) {
            errorList = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
        } else if (e instanceof ConstraintViolationException) {
            webApiResponse.setMsg(((ConstraintViolationException) e).getConstraintViolations().iterator().next().getMessage());
            return response(webApiResponse);
        }
        if (errorList.size() > 0) {
            webApiResponse.setMsg(errorList.get(0).getDefaultMessage());
            return response(webApiResponse);
        }
        logger.error("参数解析失败->", e);
        webApiResponse.setMsg("参数解析失败");
        return response(webApiResponse);
    }

    private ResponseEntity<String> handleWebError(HttpStatus httpStatus, Exception e) {
        logger.error("{}->", httpStatus.getReasonPhrase(), e);
        WebApiResponse<Void> webApiResponse = new WebApiResponse<>();
        webApiResponse.setCode(httpStatus.value());
        webApiResponse.setMsg(httpStatus.getReasonPhrase());
        return response(webApiResponse);
    }

}
