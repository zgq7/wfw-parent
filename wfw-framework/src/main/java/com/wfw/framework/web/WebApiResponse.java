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

    public WebApiResponse() {
        this.setCode(HttpStatus.OK.value());
        this.setMsg(HttpStatus.OK.getReasonPhrase());
    }

    public static <T> WebApiResponse<T> ok(T data) {
        WebApiResponse<T> result = new WebApiResponse<>();
        result.setData(data);
        return result;
    }

    public static void main(String[] args) {
        WebApiResponse<String> webApiResponse = WebApiResponse.ok("1");
        System.out.println(webApiResponse);
    }

}
