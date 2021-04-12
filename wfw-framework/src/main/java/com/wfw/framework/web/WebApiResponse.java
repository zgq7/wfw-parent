package com.wfw.framework.web;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

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

    public static <T> WebApiResponse<T> ok() {
        WebApiResponse webApiResponse = new WebApiResponse();
        webApiResponse.setCode(HttpStatus.OK.value());
        webApiResponse.setMsg(HttpStatus.OK.getReasonPhrase());
        return new WebApiResponse<>();
    }

    public static <T> WebApiResponse<T> ok(T data) {
        WebApiResponse<T> result = WebApiResponse.ok();
        result.setData(data);
        return result;
    }


}
