package com.wfw.authcommon.helper;

import com.alibaba.fastjson.JSON;
import com.wfw.framework.exception.ServiceException;
import com.wfw.framework.web.WebApiResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liaonanzhou
 * @date 2021/5/17 14:03
 * @description
 **/
public class FilterHelper {

    /**
     * @date 2020/3/3 14:21
     **/
    public static void errorResponse(HttpServletResponse response, ServiceException e) {
        responseWriter(response, WebApiResponse.build(e.getCode(), e.getMsg()));
    }

    /**
     * @apiNote 直接返回
     */
    public static void successResponse(HttpServletResponse response, WebApiResponse responseResult) {
        response.setStatus(HttpStatus.OK.value());
        responseWriter(response, responseResult);
    }

    /**
     * @apiNote 构建response 的回应
     */
    private static void responseWriter(HttpServletResponse response, WebApiResponse responseResult) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(JSON.toJSON(responseResult));
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            IOUtils.closeQuietly(out);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }


    /**
     * @apiNote 获取请求携带的cookie 集合并转化为map
     */
    private static Map<String, Cookie> getCookieMap(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return cookies == null ? Collections.emptyMap() : Arrays.stream(cookies)
                .collect(Collectors.toMap(Cookie::getName, cok -> cok,
                        (v1, v2) -> v1.getPath().equals("/") ? v1 : v2
                ));
    }

    /**
     * @apiNote 获取请求投中指定名称的cookie
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        return getCookieMap(request).get(name);
    }

}
