package com.wfw.framework.web;

import com.wfw.framework.exception.ServiceException;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

import java.beans.Transient;

/**
 * @Author liaonanzhou
 * @Date 2021/2/3 15:14
 * @Description
 */
@Data
@Accessors(chain = true)
public class WebApiResponse<T> {

    private int code;

    private T data;

    private String msg;

    @Transient
    public boolean isOk() {
        return this.code == HttpStatus.OK.value();
    }

    public static <T> WebApiResponse<T> ok() {
        WebApiResponse<T> webApiResponse = new WebApiResponse<>();
        webApiResponse.setCode(HttpStatus.OK.value());
        webApiResponse.setMsg(HttpStatus.OK.getReasonPhrase());
        return webApiResponse;
    }

    public static <T> WebApiResponse<T> ok(T data) {
        WebApiResponse<T> result = WebApiResponse.ok();
        result.setData(data);
        return result;
    }

    public static <T> WebApiResponse<T> build(int code, String msg) {
        WebApiResponse<T> result = new WebApiResponse<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <T> WebApiResponse<T> build(ServiceException serviceException) {
        WebApiResponse<T> result = new WebApiResponse<>();
        result.setCode(serviceException.getCode());
        result.setMsg(serviceException.getMsg());
        return result;
    }


}
